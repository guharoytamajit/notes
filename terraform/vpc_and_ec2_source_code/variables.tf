variable "region" {
  default     = "eu-west-1"
  description = "AWS Region"
}

variable "remote_state_bucket" {
  default     = "vpc-ec2-infra-terraform-state"
  description = "Remote bucket for state file"
}

variable "remote_state_key" {
  default     = "infrastructure.tfstate"
  description = "Remote state file to read"
}

variable "vpc_cidr" {
  description = "CIDR Block for VPC"
}

variable "public_subnet_1_cidr" {
  description = "CIDR Block for Public Subnet 1"
}

variable "public_subnet_2_cidr" {
  description = "CIDR Block for Public Subnet 2"
}

variable "public_subnet_3_cidr" {
  description = "CIDR Block for Public Subnet 3"
}

variable "private_subnet_1_cidr" {
  description = "CIDR Block for Private Subnet 1"
}

variable "private_subnet_2_cidr" {
  description = "CIDR Block for Private Subnet 2"
}

variable "private_subnet_3_cidr" {
  description = "CIDR Block for Private Subnet 3"
}

variable "ec2_key_pair_name" {
  default     = "myEC2KeyPair"
  description = "Key pair for connecting to launched EC2 instances"
}

variable "ec2_instance_type" {
  description = "EC2 Instance type to launch"
}

variable "ec2_min_instance_size" {
  description = "Minimum number of instances to launch in AutoScaling Group"
}

variable "ec2_max_instance_size" {
  description = "Maximum number of instances to launch in AutoScaling Group"
}

variable "tag_production" {
  default = "Production"
}

variable "tag_webapp" {
  default = "WebApp"
}

variable "tag_backend" {
  default = "Backend"
}