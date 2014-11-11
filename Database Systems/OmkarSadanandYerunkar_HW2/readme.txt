Name:- Omkar Sadanand Yerunkar

I did not make any changes for the co-ordinates. I have used the building.xy and firehydrant.xy file as reference to insert data. 

I am attaching all the .jar files. We need to create a class in Eclipse and store the HW2.java file in default package. 
In the workspace, we store all the HW2.java,HW2.class and all the .jar files in the folder src created automatically in the workspace.
Once this is done, we create an environment variable called CLASSPATH and specify the path of all the .jar files mentioned above separated by a semi-colon(;). 
Also once we have loaded this HW2.java file in Eclipse, we import all the .jar files and configure them as referenced libraries.

Once all the above steps are done we are good to go.

In command prompt, we go to the src folder where all the files are placed.
We run, javac HW2.java for compiling the java file
Then we run,

java -classpath .;* HW2 window building 10 20 300 500 and so on.


Note:- Make sure you connect properly to the database or else the program wont work. I have defined a function called connect(). Inside the functions' try block, we can alter the connection settings as per your localhost name, port no.,SID and password.