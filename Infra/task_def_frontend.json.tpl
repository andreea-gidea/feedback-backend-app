[
  {
    "name": "task_definition_frontend",
    "image": "${docker_image}",
    "essential": true,
    "logConfiguration": {
      "logDriver": "awslogs",
      "options": {
        "awslogs-region": "${aws_region}",
        "awslogs-group" : "/ecs/endavibe_FE7",
        "awslogs-stream-prefix": "ecs"
      }
    },
    "portMappings": [
      {
        "containerPort": 3000,
        "hostPort": 3000,
        "protocol": "tcp"
      }
    ],
    "environment": [
      {
        "name": "REACT_APP_API",
        "value" :"https://endavibe-backend.bvdbench.net"
      }
    ],
    "ulimits": [],
    "mountPoints": [],
    "volumesFrom": []
  }
]
