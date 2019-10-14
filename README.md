# bayes-spam-filter
Naive Bayes Spam Filter implementation in Java.
=======
### Requiremnets
* Java 11 or greater
### Usage
* place the required zip into `./data/` and/or change the `data` variable in the `BayesSpamFilterStarter.java`
* run using IDE, `gradlew.bat run` on windows or `./gradlew run` with Git Bash or Linux.
* for more detailed output set the `isDebug` variable to true in `BayerSpamFilterStarter.java`
* feel free to play with the first few variables in the main method of `BayerSpamFilterStarter.java`
### Results
We have chosen a PR_S of 0.5 and a SPAM_RECOGNITION_THRESHOLD of 0.9 as it leads to the best precision, which in this case, is more important than the recall.
See https://en.wikipedia.org/wiki/Precision_and_recall for further information.

```
> Task :run
BayesSpamFilterTrain.PR_S = 0.5
BayesSpamFilter.THRESHOLD = 0.9
Training Set: 
Document count Tuple2{t1=1152, t2=0}
Spam count Tuple2{t1=250, t2=194}
True positives 194
False negatives 56
False positives 0
True negatives 1152
Precision 1.0
Recall 0.776
Accuracy 0.9600570613409415

Validation Set
Document count Tuple2{t1=1339, t2=31}
Spam count Tuple2{t1=210, t2=168}
True positives 168
False negatives 42
False positives 31
True negatives 1308
Precision 0.8442211055276382
Recall 0.8
Accuracy 0.9528728211749515

Test Set
Document count Tuple2{t1=1511, t2=34}
Spam count Tuple2{t1=223, t2=177}
True positives 177
False negatives 46
False positives 34
True negatives 1477
Precision 0.8388625592417062
Recall 0.7937219730941704
Accuracy 0.9538638985005767

```
