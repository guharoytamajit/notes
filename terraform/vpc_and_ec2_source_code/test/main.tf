provider "aws" {
  region = "eu-west-1"
}

resource "aws_instance" "my_ec2_instance" {
  ami           = "ami-0a5e707736615003c"
  instance_type = "t2.micro"
  key_name      = "myEC2KeyPair"
}

resource "aws_eip" "ec2_elastic_ip" {
  instance = "${aws_instance.my_ec2_instance.id}"
}