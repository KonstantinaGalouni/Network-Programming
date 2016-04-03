# **Project Name**

----------
**Aggregator Manager**

# **Table of contents**
--------------


1. [Authors](#authors)
2. [Introduction](#introduction)
3. [Project Files](#project-files)
4. [Tools](#tools)
5. [Compile & Run](#compile-run)
	* [Maven Commands](#maven-commands)
6. [Database Schema](#database-schema)
7. [Implementation](#implementation)
	1. [main](#main)
		* [Part2.java](#part2java)
    2. [cache](#cache)
		* [Cache.java](#cachejava)
	3. [db](#db)
		* [Database.java](#databasejava)
		* [QueriesDB.java](#queriesdbjava)
	4. [services](#services)
		* [Services.java](#servicesjava)
		* [RegisterInfo.java](#registerinfojava)
		* [NmapJob.java](#nmapjobjava)
		* [XmlResults.java](#xmlresultsjava)
	5. [gui](#gui)
		* [GUI.java](#guijava)
		* [Login.java](#loginjava)
	6. [gui.jobs](#guijobs)	
		* [InfoSA.java](#infosajava)
	7. [gui.jobs.add](#guijobsadd)
		* [AddJob.java](#addjobjava)
		* [AddJobTable.java](#addjobtablejava)
		* [AddJobButtons.java](#addjobbuttonsjava)
	8. [gui.jobs.delete](#guijobsdelete)
		* [DeleteJob.java](#deletejobjava)
		* [DeleteJobTable.java](#deletejobtablejava)
		* [DeleteJobButtons.java](#deletejobbuttonsjava)
	9. [gui.jobs.results](#guijobsresults)
		* [AllNmapResultsTable.java](#allnmapresultstablejava)
		* [DateTimeSelector.java](#datetimeselectorjava)
		* [MonitorAllNmapResults.java](#monitorallnmapresultsjava)
		* [MonitorNmapResults.java](#monitornmapresultsjava)
		* [MoreNmapButton.java](#morenmapbuttonjava)
		* [NmapDetails.java](#nmapdetailsjava)
		* [NmapResultsTable.java](#nmapresultstablejava)
	10. [gui.monitor](#guimonitor)
		* [Monitor.java](#monitorjava)
		* [MonitorTable.java](#monitortablejava)
		* [MonitorButtons.java](#monitorbuttonsjava)
		* [MonitorTabs.java](#monitortabsjava)
		* [MonitorRequests.java](#monitorrequestsjava)
8. [Unrmashalling JAXB](#jaxb)

# **Authors**
---------
####  [Konstantina Galouni](http://anapgit.scanlab.gr/sdi1000034)
####  [Konstantinos Giannakelos](http://anapgit.scanlab.gr/sdi1000029)
####  [Marilena Michou](http://anapgit.scanlab.gr/sdi1000112)

# **Introduction**
---

Στα πλαίσια του μαθήματος καλείστε να υλοποιήσετε ένα κατανεμημένο σύστημα σάρωσης δικτύων υπολογιστών.

Το Nmap (Network Mapper) είναι ένα λογισμικό ανοικτού κώδικα που έχει ως βασικό στόχο την ανίχνευση δικτυακών συσκευών/συστημάτων και τον έλεγχό τους µε διάφορους τρόπους ως προς το λογισµικό που διαθέτουν, τις παρεχόμενος υπηρεσίες και τις ανοιχτές πόρτες στις οποίες µπορούν να συνδεθούν απομακρυσμένα νόµιµοι αλλά και κακόβουλοι χρήστες.

Όπως τα περισσότερα εργαλεία, το nmap χρησιμοποιείται τόσο από hackers που προσπαθούν να εισβάλουν στα υπολογιστικά συστήματα όσο και από τους διαχειριστές συστήματος (system administrators) προκειμένου να ανακαλύψουν αδυναμίες ασφάλειας που υπάρχουν στα συστήματα που διαχειρίζονται.

Για να προστατεύσουν τα δίκτυα υπολογιστών από τις εξωτερικές κακόβουλες σαρώσεις, οι διαχειριστές συστήματος τοποθετούν σε αυτά firewalls. Έτσι προκύπτει επιπλέον φόρτος για αυτούς, διότι θα πρέπει να συνδέονται ξεχωριστά σε κάθε δίκτυο που διαχειρίζονται για να εκτελέσουν σαρώσεις. Επομένως δημιουργείται η ανάγκη για την δημιουργία ενός λογισμικού που θα εκτελείται σε εσωτερικούς κόμβους δικτύων, το οποίο θα λαμβάνει αιτήματα για σαρώσεις και θα επιστρέφει τα αποτελέσματα αυτών στον διαχειριστή.


# **Project Files**
-------------

    **main**   | **cache**  |     **db**     |    **services**   |
    ---------- | ---------- | -------------- | ----------------- |
    Part2.java | Cache.java | Database.java  | Services.java     |
        -      |       -    | QueriesDB.java | NmapJob.java      |
        -      |       -    |       -        | RegisterInfo.java |
        -      |       -    |       -        | XmlResults.java   |

<br>

|  **gui**   | **gui.jobs** |   **gui.monitor**    |  **gui.jobs.add**  |  **gui.jobs.delete**  | **guis.jobs.results** 
| ---------- | -------------| -------------------- | ------------------ | --------------------- | --------------- 
| GUI.java   | InfoSA.java  | Monitor.java         | AddJob.java        | DeleteJob.java        | AllNmapResultsTable.java
| Login.java |      -       | MonitorTable.java    | AddJobTable.java   | DeleteJobTable.java   | DateTimeSelector.java
|     -      |      -       | MonitorButtons.java  | AddJobButtons.java | DeleteJobButtons.java | MonitorAllNmapResults.java
|     -      |      -       | MonitorTabs.java     |         -          |           -           | MonitorNmapResults.java
|     -      |      -       | MonitorRequests.java |         -          |           -           | MoreNmapButton.java
|     -      |      -       |           -          |         -          |           -           | NmapDetails.java
|     -      |      -       |           -          |         -          |           -           | NmapResultsTable.java

# **Tools**
------------------

**Development Tools:** &nbsp; Eclipse Luna 4.4

**Java Version:** &nbsp; 1.7 

**Operating System:** &nbsp; Linux Ubuntu 14.04

**Database:** &nbsp; MySQL

**HTTP Server:** &nbsp; GlassFish (Grizzly)

**Libraries:** &nbsp; Jersey REST API, Java Swing

**External Libraries:** &nbsp; Google Gson

# **Compile & Run**
-------------------
### Maven Commands

    cd AM/                      # cd to working directory
	mvn clean install           # cleans, compiles and runs the project



# **Database Schema**
------------------------
<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ![Alt text](resources/schema.png?raw=true "Schema") <br>

# **Implementation**
-------------------------
Παρακάτω περιγράφεται η υλοποίηση για κάθε πηγαίο αρχείο ανα πακέτο. 



## ***main***

--------------------

#### **Part2.java**

Η κλάση **Part2** είναι η βασική κλάση του προγράμματος η οποία περιλαμβάνει τη συνάρτηση **main** ,από όπου και ξεκινά η εκτέλεση. Η μέθοδος main είναι υπέυθυνη για τα εξής:

**1.**	Δημιουργεί τη βάση δεδομένων ( **mydb schema** ).				<br>
**2.**	Αρχικοποιεί την **Cache** μνήμη με τα δεδομένα της βάσης.		<br>
**3.**	Εκκινεί το **GlassFish HTTP Server**.							<br>
**4.**	Εκκινεί το γραφικό περιβάλλον για το διαχειριστή ( **GUI** ).	<br>


## ***cache***

--------------------

#### **Cache.java**

Η κλάση **Cache** είναι υπεύθυνη για την cache μνήμη της εφαρμογής. Η μνήμη, αποσκοπεί στην ταχύτερη πρόσβαση δεδομένων και λειτουργεί ως εξής:

**1.**	Αρχικοποιείται με τα δεδομένα της βάσης.	<br>
**2.**  Οι αλλαγές που πραγματοποιούνται στη βάση, πραγματοποιούνται και στη μνήμη.	<br>
**3.**	Η πρόσβαση δεδομένων, γίνεται πλέον από τη μνήμη και όχι από τη βάση, αποφεύγοντας κατά αυτόν τον τρόπο περιττά ερωτήματα σε αυτή.	<br>

Η υλοποίηση της **Cache** αποτελείται από :

* **acceptedMap** : &nbsp; ένα Map(String, String[]) για την αποθήκευση των επιβεβαιωμένων Software Agents.

* **pendingMap**  : &nbsp; &nbsp; ένα Map(String, String[]) για την αποθήκευση των μη επιβεβαιωμένων Software Agents.

* **periodicMap** : &nbsp; &nbsp; ένα Map(String, LinkedHashMap(String,String[])) για την αποθήκευση των periodic nmap job για κάθε Software Agent.

* **onetimeMap**  : &nbsp; &nbsp; ένα Map(String, LinkedHashMap(String,String[])) για την αποθήκευση των onetime nmap job για κάθε Software Agent.

* **addMap**	  : &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ένα Map(String, LinkedHashMap(String,String[])) για την αποθήκευση των νέων nmap job προς εκτέλεση για κάθε Software Agent.

* **resultMap**	  : &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ένα Map(String, List(String[])) για την αποθήκευση των αποτελεσμάτων που αντιστοιχούν στα nmap jobs ενός Software Agent.

* **xmlMap**	  : &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ένα Map(String, List(String[])) για την αποθήκευση των αποτελεσμάτων μετά το Unmarshalling.


## ***db***

--------------------

#### **Database.java**

Η κλάση **Database** είναι υπεύθυνη για τη δημιουργία της Βάσης Δεδομένων, όπου θα αποθηκεύονται όλες οι απαραίτητες πληροφορίες για τους clients (κατάσταση, jobs που εκτελούν, αποτελέσματα εκτέλεσης), καθώς και για την εισαγωγή του διαχειριστή της εφαρμογής στη Βάση αυτή, δίνοντάς του τη δυνατότητα να συνδεθεί και να εκτελέσει πλήθος ενεργειών. 
Ως βάση δεδομένων χρησιμοποιείται η MySQL και για την επικοινωνία με αυτήν μέσω της java γίνεται με τη βοήθεια του πρωτοκόλλου jdbc. Η σύνδεση στη βάση γίνεται σε τοπικό επίπεδο, συνεπώς χρησιμοποιείται η local loopback ip (127.0.0.1) και η προκαθορισμένη θύρα (3306). Τα στοιχεία επικοινωνίας είναι ως user: root και ως password: root, τα οποία είναι και αυτά που εισάγουμε στη βάση μας ως στοιχεία σύνδεσης του διαχειριστή.
Η κλάση αυτή διαβάζει το αρχείο db.sql, το οποίο περιέχει το script που περιγράφει το database schema που δημιουργήθηκε στο εργαλείο MySQL Workbench, και εκτελεί μία μία τις εντολές που περιέχει, δημιουργώντας έτσι τη βάση μας. Στη συνέχεια, εισάγει το διαχειριστή στη βάση με τα στοιχεία που αναφέρθηκαν προηγουμένως. Ο διαχειριστής αρχικά θεωρείται ανενεργός, και για λόγους ασφάλειας ο κωδικός του αποθηκεύεται σε κωδικοποιημένη μορφή (SHA-256).
<br>
Αξίζει να σημειωθεί ότι η εισαγωγή του διαχειριστή στη βάση (όπως και κάθε προσπέλασή της στην εφαρμογή) έχει υλοποιηθεί με prepared statement, προκειμένου να αποφευχθεί ο κίνδυνος του sql injection.

--------------------

#### **QueriesDB.java**

Η κλάση **QueriesDB** περιέχει τις μεθόδους-ερωτήματα για την επικοινωνία με τη βάση. Μερικές παρατηρήσεις:

* Περιέχονται οι μέθοδοι αρχικοποίησης με τα δεδομένα της βάσης, για την **Cache**.

* Οι μέθοδοι που ενημερώνουν τη βάση, ενημερώνουν κατάλληλα και την **Cache**.

* Οι μέθοδοι αποδεσμέυουν επιτυχώς τα JDBC resources μέσω της μεθόδου: <br>  <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`freeResources(Connection con, PreparedStatement stmt, ResultSet rs);`


## ***services***

--------------------

#### **Services.java**

Η κλάση **Services** περιέχει τις Web Services του Aggregator Manager. Οι υπηρεσίες περιγράφονται παρακάτω:

* **register** : &nbsp; Ο server δέχεται το request εγγραφής ενός Software Agent. Κάνει parse τα στοιχεία εγγραφής μέσω του **GSON parser**, μετατρέποντας τα **JSON** στοιχεία σε αντικείμενο **RegisterInfo**, και τον αποθηκέυει στη βάση.

* **acceptance** : &nbsp; Ο server δέχεται το request αποδοχής ενός Software Agent. Ελέγχει στην **Cache** μνήμη για την κατάσταση του εκάστοτε Software Agent, και επιστρέφει μήνυμα ανάλογα με την κατάσταση του ( confirmed, pending, declined ).

* **requestjobs** : &nbsp; Ο server δέχεται το request για την ανάθεση των nmap jobs ενός Software Agent. Ανακτεί τα jobs από την **Cache** μνήμη, τα μετατρέπει σε **JSON** μορφή μέσω του **GSON** και τα στέλνει μαζί με το response.

* **sendresults** : &nbsp; Ο server δέχεται τα αποτελέσματα εκτέλεσης ενός nmap job που ανέθεσε σε κάποιον Software Agent. Κάνει parse τα αποτελέσματα μέσω του **GSON parser**, μετατρέποντας τα **JSON** στοιχεία σε αντικείμενο **XmlResults**, και τα αποθηκέυει στη βάση μαζί με την ώρα και την ημερομηνία που τα έλαβε.


--------------------

#### **RegisterInfo.java**
Η κλάση **RegisterInfo.java** είναι απαραίτητη για την μετατροπή των **JSON** δεδομένων από τον **GSON parser**.

--------------------

#### **NmapJob.java**
Η κλάση **NmapJob.java** είναι απαραίτητη για την μετατροπή των **JSON** δεδομένων από τον **GSON parser**.

--------------------

#### **XmlResults.java**
Η κλάση **XmlResults.java** είναι απαραίτητη για την μετατροπή των **JSON** δεδομένων από τον **GSON parser**.

## ***gui***

--------------------

#### **GUI.java**
Η κλάση **GUI.java** είναι υπεύθυνη για τη δημιουργία του γραφικόυ περιβάλλοντος που χρειάζεται ο **admin** για τη διαχείριση του server.

--------------------

#### **Login.java**
Η κλάση **Login.java** αντιπροσωπεύει το μενού εισόδου που χρειάζεται ο χρήστης για να συνδεθεί στην εφαρμογή.

## ***gui.jobs***

--------------------

#### **InfoSA.java**
Η κλάση **InfoSA.java** αντιπροσωπεύει τις πληροφορίες ενός Software Agent. Αποτελείται από ένα JPanel, το οποίο περιέχει ενα JLabel με τις πληροφορίες.

--------------------

## ***gui.monitor*** 

--------------------

#### **Monitor.java**
Η κλάση **Monitor.java** αντιπροσωπεύει την κεντρική οθόνη του γραφικού περιβάλλοντος.
Δημιουργεί τα δύο κύρια JPanel ( με Box Layout για τη στοιχισμενη τοποθέτηση των στοιχείων μέσα σε αυτά) της κεντρικής οθόνης και τα ενώνει σε ενα JTabbenPane μέσω της  **MonitorTabs**.

--------------------
#### **MonitorTable.java**
Η κλάση **MonitorTable.java** αντιπροσωπεύει ένα **JPanel** με τους εγγεγραμμένους Software Agent και την κατάσταση τους ( **Online-Offline** ).
Η υλοποίηση της **MonitorTable.java** αποτελείται από:

* ένα **JTable** για την εμφάνιση των Software Agent.
* ενα **JScrollPane** για το scroll του JTable.

Για τον έλεγχο της κατάστασης των Software Agent διαβάζεται ένας χρόνος καθυστέρησης ( **waitTime** ) από το **property.dat**. 
Ο πίνακας κάθε δευτερόλεπτο ανανεώνει την κατάσταση του κάθε Software Agent, ανάλογα με το αν
ο κάθε Software Agent έχει καθυστερήσει να στειλεί αίτημα για παραπάνω από **waitTime** χρόνο ( θεωρείται Offline ).

--------------------
#### **MonitorButtons.java**
Η κλάση **MonitorButtons.java** αντιπροσωπεύει τα  **JButtons ( κουμπιά )** της **Monitor**.
Αφού ο χρήστης επιλέξει κάποιον Software Agent από τον πίνακα προβολής, μπορεί μέσω των κουμπιών να κάνει τα εξής:

* **Add a job to an Agent** 		- προσθήκη ενός ή περισσοτέρων nmap job στον agent.
* **Delete a job from an Agent** 	- διαγραφή ενός περιοδικού nmap job από τον agent.
* **View results of an Agent**		- προβολή των αποτελεσμάτων των nmap jobs του agent.
* **View results of all Agents**	- προβολή των αποτελεσμάτων των nmap jobs όλων των agent.
* **Terminate an Agent**			- τερματισμός του agent.

--------------------
#### **MonitorTabs.java**
Η κλάση **MonitorTabs** δημιουργεί ένα νέο JTabbedPane, το οποίο είναι ένα παράθυρο με καρτέλες (tabs). Στο παράθυρο αυτό τοποθετούνται δύο καρτέλες, η μία με τίτλο "Confirmed Software Agents" και η άλλη με τίτλο "Pending Software Agents". 
Όπως περιγράφουν και τα ονόματα των tabs αυτών, η πρώτη προβάλλει την κατάσταση των Software Agents που έχουν γίνει αποδεκτοί και επιτρέπει διάφορες λειτουργίες σχετικά με αυτούς (**MonitorTable**), ενώ η δεύτερη προβάλλει τους Software Agents που βρίσκονται σε αναμονή για αποδοχή ή απόρριψη (**MonitorRequests**). 
Το JTabbedPane με τη σειρά του, τοποθετείται στο JPanel που είναι το κεντρικό panel της γραφικής διεπαφής.

--------------------
#### **MonitorRequests.java**
Η κλάση **MonitorRequests** δημιουργεί ένα JScrollPane, στο οποίο τοποθετεί έναν πίνακα που απεικονίζει τις πληροφορίες των Software Agents που βρίσκονται σε αναμονή και τα κουμπιά αποδοχής και απόρριψης καθενός.
Ο πίνακας έχει τις στήλες "Hostname", "Ip Address", "Mac Address", "OS Version", "Nmap Version", "HashKey", "Accept", "Decline", όπου οι δύο τελευταίες είναι τα κουμπιά που αναφέρθηκαν παραπάνω. Κάθε κελί δε μπορεί να υποστεί επεξεργασία, αλλά μπορούμε να αντιγράψουμε το περιεχόμενό του, ενώ όταν επιλέξουμε κάποια γραμμή του πίνακα αυτή χρωματίζεται με διαφορετικό χρώμα. Όταν ένας Software Agent γίνει αποδεκτός, ολόκληρη η γραμμή του πίνακα γίνεται πράσινη και τα κουμπιά αποχρωματίζονται δηλώνοντας πως πλέον η αλλαγή έχει πραγματοποιηθεί. Αντίστοιχα, όταν ένας Software Agent απορριφθεί, το χρώμα που παίρνει η γραμμή είναι το κόκκινο.
Ο πίνακας ανανεώνεται κάθε 5 δευτερόλεπτα αυτόματα, οπότε όλες οι επιβεβαιώσεις/απορρίψεις εξαφανίζονται, ενώ εμφανίζονται εάν υπάρχουν και τα νέα αιτήματα.

--------------------
## ***gui.jobs.add***

--------------------

#### **AddJob.java**
Η κλάση **AddJob.java** αντιπροσωπεύει το παράθυρο για τη λειτουργία της εισαγωγής job σε έναν Software Agent.
Η υλοποίηση της **AddJob.java** αποτελείται από ένα JPanel το οποίο: 

* περιέχει ένα JPanel που αποτελείται από τα **AddJobTable** και **AddJobButtons** αντικέιμενα.
* την **InfoSA**.

--------------------

#### **AddJobTable.java**
Η κλάση **AddJobTable.java** αντιπροσωπεύει ένα **JPanel** με τα jobs που έχει πληκτρολογήσει ο χρήστης προς εισαγωγή.
Η υλοποίηση της **AddJobTable.java** αποτελείται από:

* ένα **JTable** για την εμφάνιση των jobs.
* ενα **JScrollPane** για το scroll του JTable.


--------------------
#### **AddJobButtons.java**
Η κλάση **AddJobButtons.java** αντιπροσωπεύει τα  **JButtons ( κουμπιά )** της **AddJob**.
Τα κουμπιά έχουν τις εξής λειτουργίες:

* **Add to list** 		- προσθήκη ενός nmap job στη λίστα πρός εισαγωγή.
* **Remove from list** 	- διαγραφή ενός nmap job από τη λίστα πρός εισαγωγή.
* **Submit jobs**		- ανάθεση της λίστας των job στον Software Agent.

Κατά την προσθήκη ενός job, γίνεται οι απαραίτητοι έλεγχοι εγκυρότητας πρίν προστεθεί στη λίστα.

--------------------

## **gui.jobs.delete** 
--------------------

#### **DeleteJob.java**
Η κλάση **DeleteJob.java** αντιπροσωπεύει το παράθυρο για τη λειτουργία της διαγραφής ενός περιοδικού job από έναν Software Agent.
Η υλοποίηση της **DeleteJob.java** αποτελείται από ένα JPanel το οποίο: 

* περιέχει ένα JPanel που αποτελείται από τα **DeleteJobTable** και **DeleteJobButtons** αντικέιμενα.
* την **InfoSA**.

--------------------

#### **DeleteJobTable.java**
Η κλάση **DeleteJobTable.java** αντιπροσωπεύει ένα **JPanel** με τα περιοδικά jobs που εκτελούνται στον επιλεγμένο Software Agent.
Η υλοποίηση της **DeleteJobTable.java** αποτελείται από: 

* ένα **JTable** για την εμφάνιση των περιοδικών jobs.
* ενα **JScrollPane** για το scroll του JTable.

--------------------
#### **DeleteJobButtons.java**
Η κλάση **DeleteJobTable.java** αντιπροσωπεύει το  **JButtons ( κουμπί )** της **DeleteJob**.
Τα κουμπιά έχουν τις εξής λειτουργίες:

* **Delete selected job**		- διαγραφή του επιλεγμένου job.

--------------------

## ***gui.jobs.results*** 

--------------------
#### **AllNmapResultsTable.java**
Η κλάση **AllNmapResultsTable** δημιουργεί ένα JScrollPane, στο οποίο τοποθετεί έναν πίνακα που απεικονίζει τα αποτελέσματα εκτέλεσης των nmap jobs όλων των Software Agents.
Ο πίνακας έχει τις στήλες "Hostname", "IdNmapJob", "idJobResult", "Time". Κάθε κελί δε μπορεί να υποστεί επεξεργασία, αλλά μπορούμε να αντιγράψουμε το περιεχόμενό του, ενώ όταν επιλέξουμε κάποια γραμμή του πίνακα αυτή χρωματίζεται με διαφορετικό χρώμα.
Ο πίνακας ανανεώνεται κάθε 5 δευτερόλεπτα αυτόματα, οπότε εμφανίζονται εάν υπάρχουν και τα νέα αποτελέσματα.

--------------------
#### **DateTimeSelector.java**
Η κλάση **DateTimeSelector** δημιουργεί ένα JSpinner το οποίο περιλαμβάνει την ώρα και την ημερομηνία με τη μορφή yyyy-MM-dd HH:mm:ss. Η αρχική τιμή του είναι η τρέχουσα ημερομηνία και ώρα.

--------------------
#### **MonitorAllNmapResults.java**
Η κλάση **MonitorAllNmapResults** εμφανίζει τα αποτελέσματα όλων των nmap jobs, τα spinners για τη δυνατότητα περιορισμού των αποτελεσμάτων που προβάλλονται διαλέγοντας συγκεκριμένο χρονικό διάστημα, και ένα κουμπί που εάν επιλεγεί κάποιο job από τον πίνακα και πατηθεί το κουμπί εμφανίζονται περισσότερες πληροφορίες για το αποτέλεσμα του job.

--------------------
#### **MonitorNmapResults.java**
Η κλάση **MonitorNmapResults** εμφανίζει τα αποτελέσματα όλων των nmap jobs για έναν συγκεκριμένο Software Agent, κάποιες βασικές πληροφορίες για τον συγκεκριμένο Software Agent, τα spinners για τη δυνατότητα περιορισμού των αποτελεσμάτων που προβάλλονται διαλέγοντας συγκεκριμένο χρονικό διάστημα, και ένα κουμπί που εάν επιλεγεί κάποιο job από τον πίνακα και πατηθεί το κουμπί εμφανίζονται περισσότερες πληροφορίες για το αποτέλεσμα του job.

--------------------
#### ***MoreNmapButton.java***
Η κλάση **MoreNmapButton** δημιουργεί ένα κουμπί που αν πατηθεί εμφανίζεται ένα νέο παράθυρο με όλες τις βασικές πληροφορίες του αποτελέσματος του επιλεγμένου job που αντλήθηκαν μέσω unmarshalling του xml.

--------------------
#### ***NmapDetailsjava***
Η κλάση **NmapDetails** εμφανίζει το παράθυρο που αναφέρθηκε προηγουμένως με όλες τις βασικές πληροφορίες του αποτελέσματος του επιλεγμένου job που αντλήθηκαν μέσω unmarshalling του xml.

--------------------
#### ***NmapResultsTable.java***
Η κλάση **NmapResultsTable** δημιουργεί ένα JScrollPane, στο οποίο τοποθετεί έναν πίνακα που απεικονίζει τα αποτελέσματα εκτέλεσης των nmap jobs ενός συγκεκριμένου Software Agent που επιλέχθηκε σε προηγούμενη οθόνη.
Ο πίνακας έχει τις στήλες "IdNmapJob", "IdJobResult", "Time". Κάθε κελί δε μπορεί να υποστεί επεξεργασία, αλλά μπορούμε να αντιγράψουμε το περιεχόμενό του, ενώ όταν επιλέξουμε κάποια γραμμή του πίνακα αυτή χρωματίζεται με διαφορετικό χρώμα.
Ο πίνακας ανανεώνεται κάθε 5 δευτερόλεπτα αυτόματα, οπότε εμφανίζονται εάν υπάρχουν και τα νέα αποτελέσματα.

--------------------
# **JAXB**
Από την κεντρική σελίδα του nmap ανακτήθηκε το αρχείο nmap.dtd, το οποίο περιγράφει κάθε πιθανή μορφή του xml αποτελέσματος μιας εντολής nmap. Έπειτα, με την εντολή xjc -dtd -p jaxb nmap.dtd δημιουργήθηκαν αυτόματα όλες οι java κλάσεις από το dtd αρχείο και τοποθετήθηκαν στο φάκελο jaxb. 
To xml parsing έγινε με τη βοήθεια του jaxb Unmarshaller, όπου διαβάζεται το xml string και είτε απευθείας αντλείται η πληροφορία, είτε ελέγχεται ποιάς κλάσης στιγμιότυπο είναι το στοιχείο που διαβάστηκε, ώστε να ψάξουμε βαθύτερα στις παραγόμενες κλάσεις.
Το unmarshalling εκτελείται στην κλάση QueriesDB από τη συνάρτηση unmarshallXml, και τα αναλυτικά αποτελέσματα που προκύπτουν αποθηκεύονται στην cache.
