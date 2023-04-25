# J-SSH-app

###Still in progress :P


Application to connect to SSH. It reads data from Excel file, stored in way like this:

#Example Excel FILE

|   Hostname    |   IP          |
| ------------- |:-------------:|
| platform1     | 10.0.01       |
| platform2     | 192.168.08    |
| platform3     | 192.211.212.1 |

#HOW IT WORKS

The program create UI, with 3 columns:
 - Hostname
 - IP
 - Status
 
 Hostname and IP are excel table equivalents. The third called status, is additional. The program can check if the SSH port is open, and then prints it status in 
 this column. It doesn't work immediately, you need to wait at start to see it. It depends on how many platforms you got. After first reading and pasting of 
 platform statuses it's doing again the same operation every 5 second.
 
 In login bar, you can add your login for ssh. This causes the command to be called in the following way - " ssh your_login@ip "
 
 You achieve connection by just clicking on the selected row.
 
 The program is working with using of Windows Terminal app. But you change it to standalone Powershell/CMD by changing this line:
 
```java
 ProcessBuilder builder = new ProcessBuilder("wt.exe", "powershell", "ssh " + login.getText() + "@" + ipaddr);
 ```
 for example for CMD
 
 ```java
  ProcessBuilder builder = new ProcessBuilder("cmd.exe", "ssh " + login.getText() + "@" + ipaddr);
  ```
 There is also a dynamic search filter, which shows only the rows, that are matching your input.
  
 
