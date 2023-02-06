#--------------------------------------------------------------------------------------
#task_definition
#--------------------------------------------------------------------------------------
resource aws_cloudwatch_log_group lg_task_definition {
  tags = {
    Name = format("%s_%s","cloudwatch_log_group", var.tags)
  }
}
data "template_file" "task_definition" {
  template = file("task_def_backend.json.tpl")
  vars = {
    docker_image              = "${data.aws_caller_identity.current.account_id}.dkr.ecr.${var.region}.amazonaws.com/envaviberegistry:docker_image_deploy_backend"
    aws_region                = var.region
    POSTGRESSQL_DATABASENAME  = var.POSTGRESSQL_DATABASENAME
    POSTGRESSQL_HOST          = aws_db_instance.endavahub-postgres.address
    POSTGRESSQL_PASSWORD      = var.POSTGRESSQL_PASSWORD
    POSTGRESSQL_PORT          = var.POSTGRESSQL_PORT
    POSTGRESSQL_USERNAME      = var.POSTGRESSQL_USERNAME
  }
}
resource "aws_ecs_task_definition" "task_definition" {
  family                   = format("%s_%s", var.cluster_name, "task_definition")
  network_mode             = "awsvpc"
  execution_role_arn       = data.aws_iam_role.role_execution.arn
  task_role_arn            = data.aws_iam_role.role_taskdef.arn
  cpu                      = 2048
  memory                   = 4096
  requires_compatibilities = ["FARGATE"]
  container_definitions    =  data.template_file.task_definition.rendered
  tags = {
    Name = format("%s_%s","task_definition_backend", var.tags)
  }
}

resource "aws_ecs_service" "service_task_definition" {
  name             = format("%s_%s", var.cluster_name, "service_task_definition")
  cluster          = "fargateclusterendavibe"
  task_definition  = aws_ecs_task_definition.task_definition.arn
  desired_count    = 1
  launch_type      = "FARGATE"
  platform_version = "1.4.0"

  network_configuration {
    security_groups  = [aws_security_group.task_definition_sg_be.id]
    subnets          = [aws_subnet.private_subnet_endavibe[0].id, aws_subnet.private_subnet_endavibe[1].id]
    assign_public_ip = false
  }

  service_registries {
    registry_arn = aws_service_discovery_service.endavibe-service.arn
  }
   
    
  load_balancer {
    target_group_arn = aws_lb_target_group.tg_be.arn
    container_name   = "task_definition_backend"
    container_port   = 8081
  }
      
  tags = {
    Name = format("%s_%s","Service_taskdefinition_backend", var.tags)
  }
  # depends_on = [aws_iam_role_policy_attachment.ecs_task_attachement, aws_lb_target_group.task_definition]
}

# resource "aws_appautoscaling_target" "autoscaling_target_task_definition" {
#   max_capacity       = 5
#   min_capacity       = 1
#   resource_id        = "service/${var.cluster_name}/${aws_ecs_service.service_task_definition.name}"
#   scalable_dimension = "ecs:service:DesiredCount"
#   service_namespace  = "ecs"
# }

# resource "aws_appautoscaling_policy" "autoscaling_policy_task_definition" {
#   name               = format("%s_%s_%s", var.cluster_name, "autoscaling_policy_task_definition", local.env)
#   policy_type        = "TargetTrackingScaling"
#   resource_id        = aws_appautoscaling_target.autoscaling_target_task_definition.resource_id
#   scalable_dimension = aws_appautoscaling_target.autoscaling_target_task_definition.scalable_dimension
#   service_namespace  = aws_appautoscaling_target.autoscaling_target_task_definition.service_namespace

#   target_tracking_scaling_policy_configuration {
#     predefined_metric_specification {
#       predefined_metric_type = "ECSServiceAverageMemoryUtilization"
#     }
#     target_value = 60
#   }
# }

# resource "aws_lb_listener_rule" "listener_task_definition_redirect_all" {
#   listener_arn = var.alb_listener_arn

#   action {
#     type             = "forward"
#     target_group_arn = aws_lb_target_group.task_definition.arn
#   }

#   condition {
#     host_header  {
#       values = ["kpi.pacts.cloud"]
#     }
#   }
#   condition {
#     path_pattern {
#       values = ["/grafana", "/grafana/*"]
#     }
#   }



