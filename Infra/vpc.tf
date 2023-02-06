resource "aws_vpc" "vpc_endavibe" {
  cidr_block       = var.cidr_block
  enable_dns_support   = true
  enable_dns_hostnames = true            
  instance_tenancy = "default"
  tags = {
    Name = "vpc_endavibe"
  }
}

# data "aws_acm_certificate" "certificate_lb"{
#   domain = "endavibe.com"
#   statuses = ["ISSUED"]
# }

data "aws_ecr_image" "backend_DockerImage" {
  repository_name = "envaviberegistry"
  image_tag       = "docker_image_deploy_backend"
}

data "aws_iam_role" "role_execution"{
  name = "ecsTaskExecutionRole"
  # arn = "arn:aws:iam::669788234127:role/ecsTaskExecutionRole"

}
data "aws_iam_role" "role_taskdef"{
  name = "role_taskdefinition"
  # arn = "arn:aws:iam::669788234127:role/role_taskdefinition"

}

data "aws_caller_identity" "current" {}