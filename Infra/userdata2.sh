#!/bin/bash
sudo sed 's/PasswordAuthentication no/PasswordAuthentication yes/' -i /etc/ssh/sshd_config
sudo systemctl restart sshd
sudo service sshd restart

#TODO: replace endavibedatabase with your desired username
sudo useradd endavibedatabase
# TODO: replace password123 with desired password and change endavibedatabase to your username chosen in useradd 
echo "endavibedatabase:password" | sudo chpasswd