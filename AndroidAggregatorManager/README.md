# **Project Name**

----------
**Aggregator Manager - Android Application**

# **Table of contents**
--------------


1. [Authors](#authors)
2. [Introduction](#introduction)
3. [Project Files](#project-files)
4. [Tools](#tools)
5. [Compile & Run](#compile-run)
	* [Maven Commands](#maven-commands)
6. [Implementation](#implementation)
	1. [application](#application)
		* [App.java](#appjava)
	2. [sqlite](#sqlite)
		* [SQLiteHelper.java](#sqlitehelperjava)
		* [SQLiteDB.java](#sqlitedbjava)
	3. [utility](#utility)
		* [NetworkChangeReceiver.java](#networkchangereceiverjava)
		* [NetworkUtils.java](#networkutilsjava)
		* [Selected.java](#selectedjava)
		* [Utils.java](#utilsjava)
	4. [main](#main)
		* [MainActivity.java](#mainactivityjava)
		* [HeaderFragment.java](#headerfragmentjava)
		* [StatusRequest.java](#statusrequestjava)
    5. [login](#login)
		* [LoginActivity.java](#loginactivityjava)
		* [LoginRequest.java ](#loginrequestjava)
		* [RegisterActivity.java](#registeractivityjava)
		* [RegisterRequest.java](#registerrequestjava)
	6. [monitor](#monitor)
		* [MonitorFragment.java](#monitorfragmentjava)
		* [MonitorRequest.java](#monitorrequestjava)
		* [TerminateRequest.java](#terminatejava)
	7. [add](#add)
		* [AddFragment.java ](#addfragmentjava)
		* [AddRequest.java](#addrequestjava)
		* [AddHistory.java](#addhistoryjava)
		* [HistoryRequest.java](#historyrequestjava)
		* [Validator.java](#validatorjava)
	8. [delete](#delete)	
		* [DeleteFragment.java](#deletefragmentjava)
		* [DeleteRequest.java ](#deleterequestjava)
		* [DeleteListRequest.java](#deletelistrequestjava)

	9. [results](#results)
		* [AllResultsFragment.java](#allresultsfragmentjava)
		* [AllResultsRequest.java](#allresultsrequestjava)
		* [ResultsFragment.java](#resultsfragmentjava)
		* [ResultsRequest.java](#resultsrequestjava)
		* [DetailsRequest.java](#detailsrequestjava)


<br><br>


# **Authors**
---------
####  [Konstantina Galouni](http://anapgit.scanlab.gr/sdi1000034)
####  [Konstantinos Giannakelos](http://anapgit.scanlab.gr/sdi1000029)
####  [Marilena Michou](http://anapgit.scanlab.gr/sdi1000112)


<br><br>


# **Introduction**
---

Στα πλαίσια του μαθήματος καλείστε να υλοποιήσετε ένα κατανεμημένο σύστημα σάρωσης δικτύων υπολογιστών.

Το Nmap (Network Mapper) είναι ένα λογισμικό ανοικτού κώδικα που έχει ως βασικό στόχο την ανίχνευση δικτυακών συσκευών/συστημάτων και τον έλεγχό τους µε διάφορους τρόπους ως προς το λογισµικό που διαθέτουν, τις παρεχόμενος υπηρεσίες και τις ανοιχτές πόρτες στις οποίες µπορούν να συνδεθούν απομακρυσμένα νόµιµοι αλλά και κακόβουλοι χρήστες.

Όπως τα περισσότερα εργαλεία, το nmap χρησιμοποιείται τόσο από hackers που προσπαθούν να εισβάλουν στα υπολογιστικά συστήματα όσο και από τους διαχειριστές συστήματος (system administrators) προκειμένου να ανακαλύψουν αδυναμίες ασφάλειας που υπάρχουν στα συστήματα που διαχειρίζονται.

Για να προστατεύσουν τα δίκτυα υπολογιστών από τις εξωτερικές κακόβουλες σαρώσεις, οι διαχειριστές συστήματος τοποθετούν σε αυτά firewalls. Έτσι προκύπτει επιπλέον φόρτος για αυτούς, διότι θα πρέπει να συνδέονται ξεχωριστά σε κάθε δίκτυο που διαχειρίζονται για να εκτελέσουν σαρώσεις. Επομένως δημιουργείται η ανάγκη για την δημιουργία ενός λογισμικού που θα εκτελείται σε εσωτερικούς κόμβους δικτύων, το οποίο θα λαμβάνει αιτήματα για σαρώσεις και θα επιστρέφει τα αποτελέσματα αυτών στον διαχειριστή.


<br><br>


# **Project Files**
-------------

 **application**		|		**sqlite** 		| 	**utility**  	|
 ---------------------- | ---------------------	| ----------------- |
     App.java			|	SQLiteHelper.java 	| NetworkChangeReceiver.java
		-				|	SQLiteDB.java		| NetworkUtils.java
		-				|			-			| Selected.java
		-				|			-			| Utils.java
-------------------------- |--------------------------| -----------------------

<br>

   		**monitor**		| 	**main**			|   **login**    |
 ----------------------	| --------------------- | --------------|
 MonitorFragment.java   | MainActivity.java		| LoginActivity.java 
 MonitorRequest.java   	| HeaderFragment.java	| LoginFragment.java
 TerminateRequest.java  | StatusRequest.java	| LoginRequest.java
 		-				| 			-			| RegisterActivity.java
		-				|			-			| RegisterFragment.java
		-				| 	 		-			| RegisterRequest.java
		-				| 			-			| AndroidInfo.java	
-----------------------	|-----------------------| -----------------------

<br>

	**add**  		|  		**delete**  	| **results** 
 ------------------ | --------------------- | --------------- 
 AddFragment.java   | DeleteFragment.java   | AllResultsFragment.java
 AddRequest.java    | DeleteRequest.java    | AllResultsRequest.java
 AddHistory.java    | DeleteListRequest.java| ResultsFragment.java
 HistoryRequest.java| 			-      		| ResultsRequest.java
 Validator.java     | 			-       	| DetailsRequest.java
-----------------------	|-----------------------| -----------------------


<br><br>


# **Tools**
------------------

**Development Tools:** &nbsp; Eclipse Luna 4.4

**Java Version:** &nbsp; 1.7 

**Operating System:** &nbsp; Linux Ubuntu 14.04

**AndroidVersion:** &nbsp; 4.4.2

**Database:** &nbsp; SQLite

**Libraries:** &nbsp; Apache HttpClient , Google Gson

**Other Software:** &nbsp; Apache Maven


<br><br>


# **Compile & Run**
-------------------
### Maven Commands

    cd AndroidAM/  															# cd to working directory 
	mvn clean install														# cleans and compiles the project
	mvn android:deploy android:run											# runs the project on an open emulator
	mvn clean install  android:emulator-start android:deploy android:run    # cleans, compiles and runs the project (also opens emulator with name "Nexus")


<br><br>


# **Implementation**
-------------------------
Παρακάτω περιγράφεται η υλοποίηση για κάθε πηγαίο αρχείο ανα πακέτο. 


<br><br><br><br>


## ***application***
------------------------




#### **App.java**

Η κλάση **App.java** δημιουργεί και αρχικοποιεί τη βάση της εφαρμογής κατά την εκκίνηση της.


<br><br><br><br>


## ***sqlite***
--------------------




#### **SQLiteHelper.java**
Η κλάση **SQLiteHelper.java** παρέχει τις βοηθητικές συναρτήσεις για τη δημιουργία της βάσης **SQLite**. 

------------------------
#### **SQLiteDB.java**
Η κλάση **SQLiteDB.java** αποτελεί τη βάση της εφαρμογής. 
Συγκεκριμένα δημιουργεί τη βάση με όνομα **"jobs.db"** και τον πίνακα **"jobs"** 
στον οποίο αποθηκέυονται όλα τα nmap jobs τα οποία δεν έφτασαν στον εκάστοτε
Software Agent λόγω απώλειας σύνδεσης. Επίσης παρέχει τις συναρτήσεις για τη
διαχείριση της βάσης (εισαγωγή-διαγραφή-ανάκτηση).


<br><br><br><br>


## ***utility***
--------------------



#### **NetworkChangeReceiver.java**
Η κλάση **NetworkChangeReceiver.java** είναι υπεύθυνη για την αποστολή των 
χαμένων	jobs όταν ανακτάται η σύνδεση στο διαδίκτυο. Επίσης ενημερώνει την
όψη **HeaderFragment** η οποία δείχνει τη συνδεσιμότητα.

------------------------
#### **NetworkUtils.java**
Η κλάση **NetworkUtils.java** παρέχει τη μέθοδο γνωστοποίησης της σύνδεσης
με το διαδίκτυο.

------------------------
#### **Selected.java**
Η κλάση **Selected.java** αποτελείται από έναν ακέραιο. Η κλάση αυτή περνιέται
ως **reference** σε άλλες συναρτήσεις (διότι ένας ακέραιος δε μπορεί να περαστεί
με **reference**) και είναι απαραίτητη για να κρατάει την επιλεγμένη γραμμή σε 
μία **TableLayout** όψη.

------------------------
#### **Utils.java**
Η κλάση **Utils.java** περιέχει μεθόδους για το κατάλληλο rendering των
**TableLayout** και του **HeaderFragment** της εφαρμογής.


<br><br><br><br>


## ***monitor***
--------------------



#### **MonitorFragment.java**
Η κλάση **MonitorFragment.java** αποτελεί τη κεντρική οθόνη (**Μonitor**) της εφαρμόγης.
Συγκεκριμένα αποτελείται από τη λίστα των Software Agents (και της κατάστασης τους) και από
ένα μενού ενεργειών. Η λίστα ανανεώνεται κάθε δύο δευτερόλεπτα μέσω ενός **MonitorRequest**.

------------------------
#### **MonitorRequest.java**
Η κλάση **MonitorRequest.java** είναι υπέυθυνη για την διεκπαιρέωση ενός request στον
**Aggregator Manager**. Το request ζήτα μέσω **GET** από τον **AM** την τωρινή κατάσταση 
της κεντρικής οθόνης, όπως αυτή παρουσιάζεται στον **ΑΜ**.

------------------------
#### **TerminateRequest.java**
Η κλάση **TerminateRequest.java** αποτελεί το request για τον τερματισμό ενός
**Software Agent**. Το request στέλνει το **hashKey** του εκάστοτε **SA**.


<br><br><br><br>


## ***main***
------------------------



#### **MainActivity.java**
Η κλάση **MainActivity.java** αποτελεί την κύρια δραστηριότητα της εφαρμογής. 
Δημιουργεί τα HeaderFragment και MonitorFragment και τα δείχνει στην οθόνη.

------------------------
#### **HeaderFragment.java**
Η κλάση **HeaderFragment.java** δημιουργεί την οψή-μπάρα που δείχνει την κατάσταση
της συνδεσιμότητας (**Wifi-Mobile3G-NoConnection**) και την κατάσταση 
του **Aggregator Manager (Online-Offline)**. Η συνδεσιμότητα ενημερώνεται μέσω 
της κλάσης **NetworkChangeReceiver** και η κατάσταση τους **ΑΜ** μέσω ενός περιοδικού
StatusRequest.

------------------------
#### **StatusRequest.java**
Η κλάση **StatusRequest.java** είναι το αιτήμα για έλεγχο της κατάστασης του **ΑggregatorΜanager (Online-Offline)**.


<br><br><br><br>


## ***login***



--------------------
#### **LoginActivity.java**
Η κλάση **LoginActivity.java** αποτελεί την αρχική δραστηριότητα της εφαρμογής. 
Δημιουργεί τα HeaderFragment και LoginFragment και τα δείχνει στην οθόνη.

------------------------
#### **LoginFragment.java**
Η κλάση **LoginFragment.java** αποτελεί την αρχική οθόνη (**Login**) της εφαρμόγης.
Συγκεκριμένα, περιλαμβάνει δύο κουτιά (EditText) στα οποία ο χρήστης εισάγει το username και το password του και
ένα κουμπί Login που ενεργοποιεί ένα **LoginRequest** για τον έλεγχο εάν ο χρήστης συνδέθηκε.
Ακόμα, υπάρχει και ένας σύνδεσμος προς την οθόνη **Register**.

------------------------
#### **LoginRequest.java**
Η κλάση **LoginRequest.java** είναι υπέυθυνη για την διεκπαιρέωση ενός request στον
**Aggregator Manager**. Το request ζήτα μέσω **POST** από τον **AM** την επιβεβαίωση των στοιχείων χρήστη που εισήχθησαν,
αφού συγκριθούν με τα στοιχεία του πίνακα **Users** της βάσης στον **ΑΜ**.
Εάν τα στοιχεία είναι ορθά, ο χρήστης συνδέεται και μεταβαίνει στην κεντρική οθόνη **Monitor**,
ενώ σε αντίθετη περίπτωση εμφανίζεται το κατάλληλο μήνυμα λάθους.
Σημειώνεται πως το request δεν πραγματοποιείται όταν δεν υπάρχει σύνδεση, ή το username ή το password ξεκινούν με κενό.

------------------------
#### **RegisterActivity.java**
Η κλάση **RegisterActivity.java** αποτελεί τη δραστηριότητα εγγραφής χρήστη της εφαρμογής. 
Δημιουργεί τα HeaderFragment και RegisterFragment και τα δείχνει στην οθόνη.

------------------------
#### **RegisterFragment.java**
Η κλάση **RegisterFragment.java** αποτελεί την οθόνη (**Register**) της εφαρμόγης.
Αποτελείται από τρία κουτιά (EditText) στα οποία ο χρήστης εισάγει το username, το password και την επιβεβαίωση του password 
και ένα κουμπί Register που ενεργοποιεί ένα **RegisterRequest** για τον έλεγχο εάν ο χρήστης εγγράφηκε επιτυχώς.
Ακόμα, υπάρχει και ένας σύνδεσμος προς την οθόνη **Login**.

------------------------
#### **RegisterRequest.java**
Η κλάση **RegisterRequest.java** είναι υπέυθυνη για την διεκπαιρέωση ενός request στον
**Aggregator Manager**. Το request ζήτα μέσω **POST** από τον **AM** την δημιουργία λογαριασμού χρήστη
στον πίνακα **Users** της βάσης στον **ΑΜ**, σύμφωνα με τα στοιχεία που εισάγονται από το χρήστη στη φόρμα εγγραφής.
Εάν το username δε χρησιμοποιείται ήδη, ο χρήστης εγγράφεται και μεταβαίνει στην αρχική οθόνη **Login**,
για να εισάγει τα στοιχεία σύνδεσης. Σε αντίθετη περίπτωση εμφανίζεται το κατάλληλο μήνυμα λάθους.
Σημειώνεται πως το request δεν πραγματοποιείται όταν δεν υπάρχει σύνδεση, το username ή το password ξεκινούν με κενό,
ή όταν ο κωδικός δεν είναι όμοιος με την επιβεβαίωσή του.

------------------------
#### **AndroidInfo.java**
Η κλάση **AndroidInfo.java** είναι βοηθητική και πακετάρει την πληροφορία του username και του password,
ώστε να σταλεί σε μορφή json μέσω request στον **ΑΜ**. Η κλάση περιλαμβάνει και τις κατάλληλες συναρτήσεις 
(set, get) για τα πεδία που περιλαμβάνει.

<br><br><br><br>


## ***add***



--------------------
#### **AddFragment.java**
Η κλάση **AddFragment.java** αποτελεί την όψη για τη λειτουργία της εισαγωγής nmap job.
Αποτελείται από τη λίστα των nmap job πρός εισαγωγή και τα κουμπιά λειτουργίας (add to list, add from history, delete from list, submit jobs).

------------------------
#### **AddRequest.java**
Η κλάση **AddRequest.java** αποτελεί το request **(POST)** για την εισαγωγή των nmap job σε έναν Software Agent.

------------------------
#### **AddHistory.java**
Η κλάση **AddHistory.java** αποτελεί την όψη για τη λειτουργία της εισαγωγής nmap job από το ιστορικό.	

------------------------
#### **HistoryRequest.java**
Η κλάση **HistoryRequest.java** αποτελεί το request **(GET)** για τη λήψη του ιστορικού ενός **Software Agent**.

------------------------
#### **Validator.java**
Η κλάση **Validator.java** χρησιμοποιείται για την ορθότητα των στοιχείων ενός nmap job.


<br><br><br><br>


## ***delete***
--------------------



#### **DeleteFragment.java**
Η κλάση **DeleteFragment.java** αποτελεί την όψη για τη λειτουργία της διαγραφής periodic nmap job.
Αποτελείται από τη λίστα των nmap job πρός διαγραφή, η οποία αρχικοποιήται μέσω ενός **DeleteListRequest** 
στον **Aggregator Manager**.

------------------------
#### **DeleteRequest.java**
Η κλάση **DeleteRequest.java** αποτελεί το request **(POST)** για την διαγραφή ενός nmap job από έναν Software Agent.

------------------------
#### **DeleteListRequest.java**
Η κλάση **DeleteListRequest.java** αποτελεί το request **(GET)** για τη λήψη των ενεργών 
periodic nmap job ενός **Software Agent**.


<br><br><br><br>


## ***results***
--------------------



#### **AllResultsFragment.java**
Η κλάση **AllResultsFragment.java** αποτελεί την όψη για την εμφάνιση όλων των αποτελεσμάτων
nmap της εφαρμόγης, ανεξαρτήτως Software Agent.
Συγκεκριμένα αποτελείται από τη λίστα των αποτελεσμάτων nmap και από ένα κουμπί (Nmap Details),
για την εμφάνιση ενός παραθύρου διαλόγου (**DetailsRequest**) με τις λεπτομέρειες του επιλεγμένου αποτελέσματος. 
Η λίστα ανανεώνεται κάθε δύο δευτερόλεπτα μέσω ενός **AllResultsRequest**.
Ακόμα, περιλαμβάνει ένα κουτί μέσα στον πίνακα των αποτελεσμάτων, για τον καθορισμό του αριθμού
των πιο πρόσφατων αποτελεσμάτων που επιθυμεί ο χρήστης να δει.

------------------------
#### **AllResultsRequest.java**
Η κλάση **AllResultsRequest.java** είναι υπέυθυνη για την διεκπαιρέωση ενός request στον
**Aggregator Manager**. Το request ζήτα μέσω **GET** από τον **AM** τα Ν πιο πρόσφατα nmap αποτελέσματα.
Το Ν καθορίζεται από το χρήστη, ο οποίος πρέπει να εισάγει έναν αριθμό στο αντίστοιχο EditText (by default: 0).

------------------------
#### **ResultsFragment.java**
Η κλάση **ResultsFragment.java** αποτελεί την όψη για την εμφάνιση όλων των αποτελεσμάτων
nmap της εφαρμόγης, ενός συγκεκριμένου Software Agent.
Συγκεκριμένα αποτελείται από τη λίστα των αποτελεσμάτων nmap και από ένα κουμπί (Nmap Details),
για την εμφάνιση ενός παραθύρου διαλόγου (**DetailsRequest**) με τις λεπτομέρειες του επιλεγμένου αποτελέσματος. 
Η λίστα ανανεώνεται κάθε δύο δευτερόλεπτα μέσω ενός **ResultsRequest**.
Ακόμα, περιλαμβάνει πληροφορίες σχετικά με τον SA και ένα κουτί μέσα στον πίνακα των αποτελεσμάτων, 
για τον καθορισμό του αριθμού των πιο πρόσφατων αποτελεσμάτων που επιθυμεί ο χρήστης να δει.

------------------------
#### **ResultsRequest.java**
Η κλάση **ResultsRequest.java** είναι υπέυθυνη για την διεκπαιρέωση ενός request στον
**Aggregator Manager**. Το request ζήτα μέσω **GET** από τον **AM** τα Ν πιο πρόσφατα nmap αποτελέσματα του επιλεγμένου SA.
Το Ν καθορίζεται από το χρήστη, ο οποίος πρέπει να εισάγει έναν αριθμό στο αντίστοιχο EditText (by default: 0).

------------------------
#### **DetailsRequest.java**
Η κλάση **DetailsRequest.java** είναι υπέυθυνη για την διεκπαιρέωση ενός request στον
**Aggregator Manager**. Το request ζήτα μέσω **GET** από τον **AM** τις λεπτομέρεις ενός nmap αποτελέσματος,
όπως αυτές προέκυψαν μέσω unmarshalling από τον **ΑΜ**.



