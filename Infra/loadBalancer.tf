# ---------------Load Balancer for frontend-------------------
resource "aws_security_group" "alb_sg" {
  description = "Controls access to the Application Load Balancer (ALB)"
  vpc_id      = aws_vpc.vpc_endavibe.id
  name        = "sg_lb_endavibe"

  ingress {
    protocol    = "tcp"
    from_port   = 443
    to_port     = 443
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    protocol    = "-1"
    from_port   = 0
    to_port     = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
  tags = {
    Name = format("%s_%s","sg_lb", var.tags)
  }
}

resource "aws_lb" "lb_endavibe" {
  name = "endavibeNVP"
  subnets            = [aws_subnet.public_subnet_endavibe[0].id, aws_subnet.public_subnet_endavibe[1].id]
  load_balancer_type = "application"
  internal           = false
  security_groups    = [aws_security_group.alb_sg.id]
  tags = {
    Name = format("%s_%s","lb", var.tags)
  }
}
resource "aws_lb_listener" "listener_alb" {
  load_balancer_arn = aws_lb.lb_endavibe.arn
  certificate_arn   = aws_acm_certificate.certificate_lb.arn
  port              = 443
  protocol          = "HTTPS"
  default_action {
    # type = "fixed-response"
    # fixed_response {
    #   content_type = "text/plain"
    #   message_body = "Fixed response content"
    #   status_code  = "503"

      type = "forward"
      target_group_arn = aws_lb_target_group.tg.arn
    }
}


resource "aws_lb_target_group" "tg" {
  name = "tg-befe"
  port = 3000
  target_type = "ip"
  protocol = "HTTP"
  vpc_id = aws_vpc.vpc_endavibe.id
}

# ---------------Load Balancer for backend-------------------
resource "aws_security_group" "alb_sg_be" {
  description = "Controls access to the Application Load Balancer (ALB)"
  vpc_id      = aws_vpc.vpc_endavibe.id
  name        = "sg_lb_endavibe_be"

  ingress {
    protocol    = "tcp"
    from_port   = 443
    to_port     = 443
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    protocol    = "-1"
    from_port   = 0
    to_port     = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
  tags = {
    Name = format("%s_%s","sg_lb", var.tags)
  }
}

resource "aws_lb" "lb_endavibe_be" {
  name = "endavibeNVPBE"
  subnets            = [aws_subnet.public_subnet_endavibe[0].id, aws_subnet.public_subnet_endavibe[1].id]
  load_balancer_type = "application"
  internal           = false
  security_groups    = [aws_security_group.alb_sg_be.id]
  tags = {
    Name = format("%s_%s","lb", var.tags)
  }
}
resource "aws_lb_listener" "listener_alb_be" {
  load_balancer_arn = aws_lb.lb_endavibe_be.arn
  certificate_arn   = aws_acm_certificate.certificate_lb.arn
  port              = 443
  protocol          = "HTTPS"
  default_action {
    # type = "fixed-response"
    # fixed_response {
    #   content_type = "text/plain"
    #   message_body = "Fixed response content"
    #   status_code  = "503"

      type = "forward"
      target_group_arn = aws_lb_target_group.tg_be.arn
    }
}


resource "aws_lb_target_group" "tg_be" {
  name = "tg-be"
  port = 8081
  target_type = "ip"
  protocol = "HTTP"
  vpc_id = aws_vpc.vpc_endavibe.id
  health_check {
    enabled = true 
    matcher = 403
    port = 8081
  }  
}
