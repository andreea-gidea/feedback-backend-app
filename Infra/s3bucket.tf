terraform {
  backend "s3" {
    # Replace this with your bucket name!
    bucket         = "endavibe-terraform-up-and-running-state"
    key            = "endavibe_terraform.tfstate"
    region         = "eu-central-1"
    encrypt        = true
  }
}
