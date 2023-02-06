# -----------------------Networking for backend and frontend ------------------------- 

resource "aws_internet_gateway" "gw_endavibe" {
  vpc_id = aws_vpc.vpc_endavibe.id
  tags = {
    Name = format("%s_%s","gw", var.tags)
  }
}

resource "aws_subnet" "public_subnet_endavibe" {
  count = length(var.public_subnets)
  vpc_id                  = aws_vpc.vpc_endavibe.id
  cidr_block              = var.public_subnets[count.index]
  availability_zone       = var.availability_zones[count.index]
  map_public_ip_on_launch = true
  tags = {
    Name = format("%s_%s","public_subnet", var.tags)
  }
}

resource "aws_route_table_association" "association_endavibe" {
  count = length(var.public_subnets)
  subnet_id      = aws_subnet.public_subnet_endavibe[count.index].id
  route_table_id = aws_route_table.routetable_public_endavibe[count.index].id
}

resource "aws_route_table" "routetable_public_endavibe" {
  count = length(var.public_subnets)
  vpc_id = aws_vpc.vpc_endavibe.id
  tags = {
    Name = format("%s_%s","route_table", var.tags)
  }
}
resource "aws_route" "route_public_endavibe" {
  count = length(var.public_subnets)
  route_table_id         = aws_route_table.routetable_public_endavibe[count.index].id
  destination_cidr_block = "0.0.0.0/0"
  gateway_id             = aws_internet_gateway.gw_endavibe.id
}



resource "aws_subnet" "private_subnet_endavibe" {
  count = length(var.private_subnets)
  vpc_id            = aws_vpc.vpc_endavibe.id
  cidr_block        = var.private_subnets[count.index]
  availability_zone = var.availability_zones[count.index]
  tags = {
    Name = format("%s_%s","private_subnet_befe", var.tags)
  }
}

resource "aws_route_table" "routetable_private_endavibe" {
  count = length(var.private_subnets)
  vpc_id = aws_vpc.vpc_endavibe.id
  tags = {
    Name = format("%s_%s","route_table_befe", var.tags)
  }
}
 
resource "aws_route" "route_endavibe" {
  count = length(var.private_subnets)
  route_table_id         = aws_route_table.routetable_private_endavibe[count.index].id
  destination_cidr_block = "0.0.0.0/0"
  nat_gateway_id         = aws_nat_gateway.natgw_endavibe[count.index].id
}
 

resource "aws_nat_gateway" "natgw_endavibe" {   
  count = length(var.public_subnets)
  depends_on    = [aws_internet_gateway.gw_endavibe]
  allocation_id = aws_eip.eip_nat_endavibe[count.index].id
  subnet_id     = aws_subnet.public_subnet_endavibe[count.index].id
  tags = {
    Name = format("%s_%s","nat_gw", var.tags)
  }
}
 
resource "aws_eip" "eip_nat_endavibe" {
  count = length(var.private_subnets)
  vpc = true
  tags = {
    Name = format("%s_%s","eip", var.tags)
  }
}

resource "aws_route_table_association" "association_private_endavibe" {
  count = length(var.private_subnets)
  subnet_id      = aws_subnet.private_subnet_endavibe[count.index].id
  route_table_id = aws_route_table.routetable_private_endavibe[count.index].id
}

# -----------------------Networking for aurora serverless -------------------------

resource "aws_subnet" "private_subnet_db1" {
  vpc_id = aws_vpc.vpc_endavibe.id
  cidr_block = "10.20.5.0/24"
  availability_zone = "eu-central-1a"
}

resource "aws_subnet" "private_subnet_db2" {
  vpc_id = aws_vpc.vpc_endavibe.id
  cidr_block = "10.20.6.0/24"
  availability_zone = "eu-central-1b"
}

resource "aws_db_subnet_group" "subnet_group_db" {
  name = "subgroup"
  subnet_ids = [aws_subnet.private_subnet_db1.id, aws_subnet.private_subnet_db2.id]
}