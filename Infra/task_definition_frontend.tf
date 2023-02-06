#--------------------------------------------------------------------------------------
#task_definition
#--------------------------------------------------------------------------------------
resource "aws_cloudwatch_log_group" "lg_task_definition_fe" {
  tags = {
    Name = format("%s_%s","cloudwatch_log_group", var.tags)
  }
}
data "template_file" "task_definition_fe" {
  template = file("task_def_frontend.json.tpl")
  vars = {
    docker_image              = "${data.aws_caller_identity.current.account_id}.dkr.ecr.${var.region}.amazonaws.com/envaviberegistry:docker_image_deploy_frontend"
    aws_region                = var.region
  }
}

resource "aws_ecs_task_definition" "task_definition_fe" {
  family                   = format("%s_%s", var.cluster_name, "task_definition")
  network_mode             = "awsvpc"
  execution_role_arn       = data.aws_iam_role.role_execution.arn
  task_role_arn            = data.aws_iam_role.role_taskdef.arn
  cpu                      = 2048
  memory                   = 4096
  requires_compatibilities = ["FARGATE"]
  container_definitions    =  data.template_file.task_definition_fe.rendered
  tags = {
    Name = format("%s_%s","task_definition_fontend", var.tags)
  }
}

resource "aws_ecs_service" "service_task_definition_fe" {
  name             = format("%s_%s", var.cluster_name, "service_task_definition_fe")
  cluster          = "fargateclusterendavibe"
  task_definition  = aws_ecs_task_definition.task_definition_fe.arn
  desired_count    = 1
  launch_type      = "FARGATE"
  platform_version = "1.4.0"

  network_configuration {
    security_groups  = [aws_security_group.task_definition_sg_fe.id]
    subnets          = [aws_subnet.private_subnet_endavibe[0].id, aws_subnet.private_subnet_endavibe[1].id]
    assign_public_ip = false
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.tg.arn
    container_name   = "task_definition_frontend"
    container_port   = 3000
  }

  tags = {
    Name = format("%s_%s","Service_taskdefinition_fontend", var.tags)
  }
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

resource "aws_lb_listener_rule" "listener_task_definition_redirect_all" {
  listener_arn = aws_lb_listener.listener_alb.arn

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.tg.arn
  }
  condition {
    path_pattern {values = ["/*"]}
  }
}


