# Mount NAS -> RaspberryPi

- create the folders
- set rights to folders (in the following case I allowed everyone everything - 777)

```

cd /mnt
sudo mkdir NAS
cd NAS
sudo mkdir LucaHome
sudo chmod 777 LucaHome

```

- folder structure will contain subolder for
  - camera
  - logs
  - misc
  - sources

- now edit the config file fstab

```

sudo nano /etc/fstab



//<YOUR_IP_ADDRESS>/LucaHome /mnt/NAS/LucaHome cifs username=<YOUR_USER_NAME>,password=<YOUR_PASSWORD>,uid=pi,gid=pi 0 0

```

- use your IP address of your RaspberryPi and your credentials for the NAS
- restart your raspberry and it should be mounted
  - if it is not mounted:

```

sudo mount -a

```