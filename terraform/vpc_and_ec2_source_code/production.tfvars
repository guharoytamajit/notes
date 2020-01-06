# VPC variables for production
vpc_cidr = "10.0.0.0/16"
public_subnet_1_cidr = "10.0.1.0/24"
public_subnet_2_cidr = "10.0.2.0/24"
public_subnet_3_cidr = "10.0.5.0/24"
private_subnet_1_cidr = "10.0.3.0/24"
private_subnet_2_cidr = "10.0.4.0/24"
private_subnet_3_cidr = "10.0.6.0/24"

# EC2 variables for production
ec2_instance_type = "t2.micro"
ec2_min_instance_size = 3
ec2_max_instance_size = 10