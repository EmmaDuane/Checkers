# General Student Manual for CS 171 (D1)

## Introduction
This is a general student manual for CS 171. The manual offers some useful and detailed tutorials for tools which are useful in this class. Students are encouraged to read this manual, especially transfer students who has no experience about ICS Openlab. However, if you want to learn about the grading policy and project description, please refer to your project student manual. 

## About ICS Openlab
UCI ICS school provides Openlab machines. These machines use Linux system. A student can log in to an openlab machine by SSH with his or her ICS account. 

**Attention: to create a fair environment for all students, projects for CS 171 are graded according your code's performance on an openlab machine. Any submission that doesn't compile or function properly on openlab will not be graded.** Thus, you should test your project throughly on openlab before the submission.

### Activate your ICS account
You have to activate your ICS account in person. Take your UCI Photo Student ID Card and see the Lab Assistant in the ICS computer Lab (**CS 364**). Ask the Lab Assistant to verify your identity by handing them your student ID card so that you can obtain an ICS instructional account.
After your card has been idverified by the lab assistant, you will be led to one of the Windows machine used for account activation. The lab assistant will login to the machine and start up an session for you.
Once you finished, you will be asked to supply an initial password for your account. Please remember this password.
If all goes well, your account will be created within **2 hours**. Be sure to activate it before the submission deadline to test your code.

## Use SSH to connect Openlab
### What's SSH?
SSH, also known as Secure Shell or Secure Socket Shell, is a network protocol that gives users, particularly system administrators, a secure way to access a computer over an unsecured network. It allows you to access the openlab environment remotely from your own device safely and see the command-line interface. 
### SSH on Mac OS
Mac OS users can easily use **Terminal** to set up SSH connection.
![command-line-interface-768x451.jpg](https://i.loli.net/2019/08/01/5d428f6aa34cb85709.jpg)


 1. Open "Terminal" (found under Applications -> Utilities).
 2. Input: `ssh yourUCInetID@openlab.ics.uci.edu`
 3. You will see a prompt for password. Input **ICS account** password. Note it could be different from your UCI account. Your response is hidden. After you type it press 'enter'.
 4. Then you will be led to the CLI of openlab.
 

### SSH on Windows
Windows system doesn't have a build-in tool for SSH. The most common choice is **PuTTY**, a free tool for SSH.
[PuTTY Download Link][1]

1. Open PuTTY and click 'Open' after configuration:
![Screen Shot 2019-08-01 at 12.18.52 AM.png](https://i.loli.net/2019/08/01/5d4293379d9de61423.png)

2. You might see the warning, click 'Yes'
![Screen Shot 2019-08-01 at 12.19.06 AM.png](https://i.loli.net/2019/08/01/5d429337046cf74358.png)

3. Input your UCInetID, press 'Enter'. And then input your **ICS account** password.

### Access your openlab off-campus
To access your openlab off-campus, including ACC apartments, you will need VPN. 
[VPN Download and information page][2]
Download VPN from this website, log in with your UCInetID. After successfully logging in, you will be able to use SSH to openlab off-campus. 
## Useful Linux commands
1. `man` - man is essentially the help command for Linux. When Linux was made the creators made a help page that corresponds to each command. The man command is the way to access these help pages. 
**Ex.** `man man`, `man ls`, `man cd`
2. `ls` - Stands for list structures, This command will list all structures under the current directory if no additional arguments are passed, otherwise it will list all structures under a given directory. 
**Ex.** `ls` (lists structures under your current directory), `ls /home` (will list all of the structures under the home directory)
3. `mkdir` - Stands for make directory. This command will make a directory with whatever name you choose. After executing the command you can run ls and see the new directory that has been made. 
**Ex.** `mkdir new_dir`
4. `cd` - Stands for change directory. Will change your directory to the given path. 
**Ex.** `cd` (A cd with no argument will change you to the '~' directory. This is considered a default directory in Linux, for openlab it is usually /home/<ucinetid>). 
`cd .` (This will change your current directory i.e. it does nothing)
`cd ..` (This will change you to the parent directory, i.e. one directory level above where you currently are. If you are currently in /home/<ucinetid>/my_dir and you run cd .. you will be put into /home/<ucinetid>. Also keep in mind that you can string the .. together, so if you ran cd ../../ you would then end up in /home)
5. `rmdir` - Stands for remove directory, Important to note that this only works on empty directories. 
**Ex.** `rmdir directory_to_be_removed_that_is_also_empty/`
If you want to remove a directory that is not empty, use:
`rm -rf the_directory`. However, think twice before using it since you can't undo it.

6. `pwd` - Stands for print working directory. This will print out the absolute path up to the directory you are in. 
Ex. `pwd`
7. `vim` or `vi` - Stands for Vi improved. Vi is an older version of vim. Vim is going to be your basic text editor, once you enter the vim interface type 'i' to enter the insert mode. From there you can type to make changes to the file. Once you are done hit the 'esc' button to exit insert mode. Now input 'Shift+ZZ' to write your changes to the file. 
[Interactive tutorial for Vim][3]
8. `cat` -  Stands for concatenate files. This command will take all of the contents of a given file and output them to your terminal screen. 
**Ex.** `cat myfile.py`
9. `rm` - Stands for remove. Can be used to remove both directories and files. can also recursively destroy directories recursively with the -r flag. Usually when you run rm it will prompt you if you actually want to remove a certain file. If you do not want these prompts then use the -f flag 
**Ex.** `rm -r directory_to_be_recursively_destroyed`, 
`rm -f file_to_be_removed`, `rm -rf _directory_be_destroyed_no_questions_asked`. Note: please be CAREFUL with rm -rf it can very easily destroy your whole project with the wrong input. The command is irreversible, there is no going back once you run it. 

## Environment Setup
### JetBrains Suite Introduction
To check if your code can successfully compile, you are supposed to run all code in openlab machine. However, copying all the code or editing the ﬁle using vim on openlab machine before each test can be a large and trivial workload. We highly recommend you to learn and try the JetBrains IDEs. They are: IntelliJ IDEA for Java, CLion for C++, and PyCharm for Python. 
#### Apply for Free Educational License
A UCI current student can get a free educational license for these IDEs. 

1. Go to the website of your IDE:


 [CLion][4]
 [PyCharm][5]
 [IntelliJ][6]


2. Download the Ultimate/Professional version. While downloading, go to [here][7] and apply for an educational license.


3. When the application is proceed and passed, you can login the IDE with your JetBrains account and enjoy your free use. 

#### Work remotely
You will need to run your code on openlab machines. PyCharm and CLion oﬀer the feature of using a remote interpreter. 
 Here are the oﬃcial tutorials:
[PyCharm][8]
 [CLion][9]
 
##### PyCharm
 Step 1: open 'Settings/Preferences' in your PyCharm. Search for 'Interpreter'. In "Project Interpreter", click the icon to "Add" a new interpreter.
 ![1.jpg](https://i.loli.net/2019/09/06/gjEY3uLTzPcoBZ2.jpg)
 Step 2: Select the tab "SSH Interpreter", input host as openlab host, and your UCInetID as username, click "Next".
 ![2.PNG](https://i.loli.net/2019/09/06/QdWZIz2yNPeoJik.png)
Step 3: enter your password of openlab. Click "Next".
![3.jpg](https://i.loli.net/2019/09/06/aA5pnBjtxO6CTWV.jpg)
Step 4: click the folder icon to select the path of the interpreter, since we will use Python3, look for python3 in interpreters. It should be in "/bin". Click "OK" and finish.
![4.jpg](https://i.loli.net/2019/09/06/XlAiWCHgq738eIU.jpg)
Step 5: you should see PyCharm working on some settings and upload your code to a temporary directory on openlab machine. After that, you can choose the remote interpreter in cofiguration.

#### Set script parameters
You might need to run your code in diﬀerent modes, which is represented as variables you pass in when you run it with command line. 
 Here are the oﬃcial tutorials:
[PyCharm][10]
 [IntelliJ][11]
 See program arguments. 
 [CLion][12]
 See program arguments.


  [1]: https://www.chiark.greenend.org.uk/~sgtatham/putty/
  [2]: https://www.oit.uci.edu/help/vpn/
  [3]: https://www.openvim.com/
  [4]: https://www.jetbrains.com/clion/download/
  [5]: https://www.jetbrains.com/pycharm/download/
  [6]: https://www.jetbrains.com/idea/download/
  [7]: https://www.jetbrains.com/student/
  [8]: https://www.jetbrains.com/help/pycharm/con%EF%AC%81guring-remote-interpreters-viassh.html
  [9]: https://blog.jetbrains.com/clion/2018/09/initial-remote-dev-support-clion/
  [10]: https://stackover%EF%AC%82ow.com/questions/33102272/pycharm-and-sys-argv-arguments
  [11]: https://www.jetbrains.com/help/idea/run-debug-con%EF%AC%81guration-application.html
  [12]: https://www.jetbrains.com/help/clion/run-debug-con%EF%AC%81guration-application.html