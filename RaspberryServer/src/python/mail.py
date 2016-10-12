import subprocess
import smtplib
import socket
import datetime
import sys
from email.mime.text import MIMEText

# account information to send the email
Receiver = 'RECEIVER_MAIL'
Sender = 'SENDER_MAIL'
Passphrase = 'YOUR_PASSPHRASE'
smtpserver = smtplib.SMTP('MAIL_PROVIDER', 587)
smtpserver.ehlo()
smtpserver.starttls()
smtpserver.ehlo

# Login for account
smtpserver.login(Sender, Passphrase)

# Get current date
Date = datetime.date.today()

# Data
Data = ""

# read all arguments and store in a string
for argument in range(1, len(sys.argv)):
	Data += str(sys.argv[argument])
	Data += " "

message = MIMEText(Data)

# Subject + Date
message['Subject'] = 'Message from Raspberry Pi - %s' % Date.strftime('%d %b %Y')

# Sender
message['From'] = Sender

#Receiver
message['To'] = Receiver

# Send Email
smtpserver.sendmail(Sender, [Receiver], message.as_string())
smtpserver.quit()