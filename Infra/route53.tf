resource "aws_acm_certificate" "certificate_lb" {
  domain_name               = var.dns
  subject_alternative_names = ["*.${var.dns}"]
  validation_method         = "DNS"

  tags = {
    Name = format("%s_%s","certificate", var.tags)
  }

  lifecycle {
    create_before_destroy = true
  }
}
resource "aws_route53_record" "validation" {
  for_each = {
    for dvo in aws_acm_certificate.certificate_lb.domain_validation_options : dvo.domain_name => {
      name   = dvo.resource_record_name
      record = dvo.resource_record_value
      type   = dvo.resource_record_type
    }
  }

  allow_overwrite = true
  name            = each.value.name
  records         = [each.value.record]
  ttl             = 60
  type            = each.value.type
  zone_id         = data.aws_route53_zone.selected.zone_id
}

resource "aws_acm_certificate_validation" "validation" {
  certificate_arn         = aws_acm_certificate.certificate_lb.arn
  validation_record_fqdns = [for record in aws_route53_record.validation : record.fqdn]
}



data "aws_route53_zone" "selected" {
  name         = "bvdbench.net"
  private_zone = false
}

resource "aws_route53_record" "frontend" {
  zone_id = data.aws_route53_zone.selected.zone_id
  name    = "endavibe.bvdbench.net"
  type    = "A"
  alias {
    name                   = aws_lb.lb_endavibe.dns_name
    zone_id                = aws_lb.lb_endavibe.zone_id
    evaluate_target_health = false
  }
}

resource "aws_route53_record" "backend" {
  zone_id = data.aws_route53_zone.selected.zone_id
  name    = "endavibe-backend.bvdbench.net"
  type    = "A"
  alias {
    name                   = aws_lb.lb_endavibe_be.dns_name
    zone_id                = aws_lb.lb_endavibe_be.zone_id
    evaluate_target_health = false
  }
}