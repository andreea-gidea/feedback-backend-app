resource "aws_security_group" "sg_instance" {
  name        = "allow_all"
  description = "Allow all HTTP/HTTPS traffic"
  vpc_id      = aws_vpc.vpc_endavibe.id

  ingress {
    from_port   = 0
    to_port     = 0
    protocol    = "icmp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "task_definition_sg_fe" {
  description = "allow inbound access from the ALB only"
  vpc_id      = aws_vpc.vpc_endavibe.id

  ingress {
    protocol        = "tcp"
    from_port       = 3000
    to_port         = 3000
    security_groups = [aws_security_group.alb_sg.id]
  }

  egress {
    protocol    = "-1"
    from_port   = 0
    to_port     = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
  tags = {
    Name = format("%s_%s","taskdef_sg", var.tags)
  }
}

resource "aws_security_group" "task_definition_sg_be" {
  description = "allow inbound access from the ALB only"
  vpc_id      = aws_vpc.vpc_endavibe.id

  ingress {
    protocol        = "tcp"
    from_port       = 8081
    to_port         = 8081
    security_groups = [aws_security_group.task_definition_sg_fe.id, aws_security_group.alb_sg_be.id]
  }

  egress {
    protocol    = "-1"
    from_port   = 0
    to_port     = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
  tags = {
    Name = format("%s_%s","taskdef_sg", var.tags)
  }
}

# -----------------------Security group for aurora serverless -------------------------
resource "aws_security_group" "db_sg" {
  description = "allow inbound access from port 5432"
  vpc_id      = aws_vpc.vpc_endavibe.id

  ingress {
    protocol        = "tcp"
    from_port       = 5432
    to_port         = 5432
    security_groups = [aws_security_group.alb_sg.id, aws_security_group.task_definition_sg_be.id, aws_security_group.sg_instance.id]
  }

  egress {
    protocol    = "-1"
    from_port   = 0
    to_port     = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
  tags = {
    Name = format("%s_%s","db_sg", var.tags)
  }
}