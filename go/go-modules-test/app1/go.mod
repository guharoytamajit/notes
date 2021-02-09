module example.com/hello/app1

go 1.14

replace example.com/hello/app2 => ../app2

require example.com/hello/app2 v0.0.0-00010101000000-000000000000
