variable "region" {
  default = "eu-central-1"
}

variable "cidr_block" {
  type = string
  description = "VPC CIDR"
  default = "10.20.0.0/16"
}

variable "availability_zone" {
  default = "eu-central-1a"
}
variable "availability_zones" {
  default = ["eu-central-1a", "eu-central-1b"]
  type = list
}
variable "private_subnets" {
  description = "a list of CIDRs for private subnets in your VPC, must be set if the cidr variable is defined, needs to have as many elements as there are availability zones"
  default     = ["10.20.4.0/24", "10.20.3.0/24"] 
}

variable "public_subnets" {
  description = "a list of CIDRs for public subnets in your VPC, must be set if the cidr variable is defined, needs to have as many elements as there are availability zones"
  default     = ["10.20.20.0/24", "10.20.21.0/24"]
}
variable "tags" {
  type = string
  default = "endavibe"  
}

# variable "docker_image" {
#   default = data.aws_ecr_image.backend_DockerImage
# }
variable "cluster_name" {
  default = "fargateclusterendavibe"
}
# variable "POSTGRESSQL_HOST" {
# }
variable "POSTGRESSQL_PORT" {
}
variable "POSTGRESSQL_DATABASENAME" {
}
variable "POSTGRESSQL_PASSWORD" {
}
variable "POSTGRESSQL_USERNAME" {}

variable "my_pub_key" {
}

variable "dns" {
  default = "bvdbench.net"
}

