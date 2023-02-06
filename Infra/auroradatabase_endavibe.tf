resource "aws_db_instance" "endavahub-postgres" {
  allocated_storage      = 10
  storage_type           = "gp2"
  engine                 = "postgres"
  engine_version         = "14.2"
  instance_class         = "db.t4g.micro"
  availability_zone      = "eu-central-1a"
  db_name                = var.POSTGRESSQL_DATABASENAME
  username               = var.POSTGRESSQL_USERNAME
  password               = var.POSTGRESSQL_PASSWORD
  port                   = 5432
  identifier             = "endavibe-postgres"
  vpc_security_group_ids = [aws_security_group.db_sg.id]
  skip_final_snapshot    = true
  apply_immediately      = true
  multi_az               = false

  db_subnet_group_name    = aws_db_subnet_group.subnet_group_db.name

  tags = {
    Name = format("%s_%s","database", var.tags)
  }
}

resource "aws_instance" "instance" {
  ami                         = "ami-0ebe464cf17879a5f"
  instance_type               = "t4g.nano"
  key_name                    = aws_key_pair.my-pub-key.id
  vpc_security_group_ids      = [aws_security_group.sg_instance.id]
  subnet_id                   = aws_subnet.public_subnet_endavibe[0].id
  associate_public_ip_address = true
  user_data                   = file("userdata2.sh")
  root_block_device {
    volume_size           = 50
    delete_on_termination = true
  }
  tags = {
    Name = format("%s_%s","instance", var.tags)
  }
}

resource "aws_key_pair" "my-pub-key" {
  key_name   = "my-pub-key"
  public_key = var.my_pub_key
}
# resource "aws_rds_cluster" "aurorapostgresDB" {
#   cluster_identifier      = "aurora-cluster-endavibe"
#   db_subnet_group_name    = aws_db_subnet_group.subnet_group_db.name
#   engine                  = "aurora-postgresql"
#   engine_mode             = "provisioned" 
#   database_name           = var.POSTGRESSQL_DATABASENAME  
#   enable_http_endpoint    = true  
#   master_username         = var.POSTGRESSQL_USERNAME
#   master_password         = var.POSTGRESSQL_PASSWORD
#   backup_retention_period = 1
#   vpc_security_group_ids = [aws_security_group.db_sg.id]
  
#   skip_final_snapshot     = true
  
#   serverlessv2_scaling_configuration {
#     max_capacity = 1.0
#     min_capacity = 0.5
#   } 
# }

# resource "aws_rds_cluster_instance" "instance1" {
#   cluster_identifier = aws_rds_cluster.aurorapostgresDB.id
#   instance_class     = "db.serverless"
#   engine             = aws_rds_cluster.aurorapostgresDB.engine
#   engine_version     = aws_rds_cluster.aurorapostgresDB.engine_version
# }