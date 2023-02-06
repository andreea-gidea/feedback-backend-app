resource "aws_service_discovery_private_dns_namespace" "namespace" {
  name = "endavibe.net"
  description = "Domain for all the service"
  vpc = aws_vpc.vpc_endavibe.id
}

resource "aws_service_discovery_service" "endavibe-service" {
  name = "endavibe-service"
  dns_config {
    namespace_id = aws_service_discovery_private_dns_namespace.namespace.id
    routing_policy = "MULTIVALUE"
    dns_records {
      ttl = 10
      type = "A"
    }
  }
  health_check_custom_config {
    failure_threshold = 5
  }
}