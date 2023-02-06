[
  {
    "name": "task_definition_backend",
    "image": "${docker_image}",
    "essential": true,
    "logConfiguration": {
      "logDriver": "awslogs",
      "options": {
        "awslogs-region": "${aws_region}",
        "awslogs-group" : "/ecs/endavibe_BE72",
        "awslogs-stream-prefix": "ecs"
      }
    },
    "portMappings": [
      {
        "containerPort": 8081,
        "hostPort": 8081,
        "protocol": "tcp"
      }
    ],
    "environment": [
      {
        "name": "POSTGRESSQL_DATABASENAME",
        "value" :"${POSTGRESSQL_DATABASENAME}"
      },
      {
        "name": "POSTGRESSQL_HOST",
        "value" :"${POSTGRESSQL_HOST}"
      },
      {
        "name": "POSTGRESSQL_PORT",
        "value" :"${POSTGRESSQL_PORT}"
      },
      {
        "name": "POSTGRESSQL_PASSWORD",
        "value": "${POSTGRESSQL_PASSWORD}"
      },
      {
        "name": "POSTGRESSQL_USERNAME",
        "value" :"${POSTGRESSQL_USERNAME}"
      },
      {
        "name": "docker_image",
        "value" :"${docker_image}"
      }
    ],
    "ulimits": [],
    "mountPoints": [],
    "volumesFrom": []
  }
]