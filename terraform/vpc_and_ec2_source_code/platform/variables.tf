variable "region" {
  default     = "eu-west-1"
  description = "AWS Region"
}

variable "remote_state_bucket" {
  default     = "vpc-ec2-infra-terraform-state"
  description = "Remote bucket for state file"
}

variable "remote_state_key" {
  default     = "layer1/infrastructure.tfstate"
  description = "Remote state file to read"
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