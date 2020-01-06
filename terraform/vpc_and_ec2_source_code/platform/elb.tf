resource "aws_elb" "webapp-load-balancer" {
  name            = "Production-WebApp-LoadBalancer"
  internal        = false
  security_groups = ["${aws_security_group.ec2_public_security_group.id}"]
  subnets         = [
    "${data.terraform_remote_state.network_configuration.public-subnet-1_id}",
    "${data.terraform_remote_state.network_configuration.public-subnet-2_id}",
    "${data.terraform_remote_state.network_configuration.public-subnet-3_id}"
  ]
  #instances       = ["${data.aws_instances.web-app-production-instances.ids}"]

  "listener" {
    instance_port     = 80
    instance_protocol = "HTTP"
    lb_port           = 80
    lb_protocol       = "HTTP"
  }

  health_check {
    healthy_threshold = 5
    interval = 30
    target = "HTTP:80/index.html"
    timeout = 10
    unhealthy_threshold = 5
  }
}

resource "aws_elb" "backend-load-balancer" {
  name            = "Production-Backend-LoadBalancer"
  internal        = true
  security_groups = ["${aws_security_group.elb_security_group.id}"]
  subnets         = [
    "${data.terraform_remote_state.network_configuration.private-subnet-1_id}",
    "${data.terraform_remote_state.network_configuration.private-subnet-2_id}",
    "${data.terraform_remote_state.network_configuration.private-subnet-3_id}"
  ]
  #instances       = ["${data.aws_instances.backend-production-instances.ids}"]

  "listener" {
    instance_port     = 80
    instance_protocol = "HTTP"
    lb_port           = 80
    lb_protocol       = "HTTP"
  }

  health_check {
    healthy_threshold = 5
    interval = 30
    target = "HTTP:80/index.html"
    timeout = 10
    unhealthy_threshold = 5
  }
}
