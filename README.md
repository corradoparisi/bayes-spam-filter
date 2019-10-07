# bayes-spam-filter
Naive Bayes Spam Filter implementation in Java.
=======
### Requiremnets
* Java 11 or better
### Usage
* place the required zip into `./data/` and/or change the `data` variable in the `BayesSpamFilterStarter.java`
* run using IDE or `./gradlew run`
### Results
We have choosen a PR_S of 0.5 and a THREASHOLD of 0.9 as it lead to the best precision and the precission is more inportant in this case than the recall.
```
> Task :run
BayesSpamFilterTrain.PR_S = 0.5
BayesSpamFilter.THREASHOLD = 0.9
Training Set: 
Document count Tuple2{t1=1152, t2=38}
Spam count Tuple2{t1=250, t2=179}
True positives 179
False negatives 71
False positives 38
True negatives 1114
Precision 0.8248847926267281
Recall 0.716

Validation Set
Document count Tuple2{t1=1339, t2=115}
Spam count Tuple2{t1=210, t2=182}
True positives 182
False negatives 28
False positives 115
True negatives 1224
Precision 0.6127946127946128
Recall 0.8666666666666667

Test Set
Document count Tuple2{t1=1511, t2=121}
Spam count Tuple2{t1=223, t2=192}
True positives 192
False negatives 31
False positives 121
True negatives 1390
Precision 0.6134185303514377
Recall 0.8609865470852018


```
```
> Task :run
BayesSpamFilterTrain.PR_S = 0.5
BayesSpamFilter.THREASHOLD = 0.5
Training Set: 
Document count Tuple2{t1=1152, t2=98}
Spam count Tuple2{t1=250, t2=235}
True positives 235
False negatives 15
False positives 98
True negatives 1054
Precision 0.7057057057057057
Recall 0.94

Validation Set
Document count Tuple2{t1=1339, t2=215}
Spam count Tuple2{t1=210, t2=206}
True positives 206
False negatives 4
False positives 215
True negatives 1124
Precision 0.48931116389548696
Recall 0.9809523809523809

Test Set
Document count Tuple2{t1=1511, t2=230}
Spam count Tuple2{t1=223, t2=219}
True positives 219
False negatives 4
False positives 230
True negatives 1281
Precision 0.48775055679287305
Recall 0.9820627802690582

```
```
> Task :run
BayesSpamFilterTrain.PR_S = 0.8
BayesSpamFilter.THREASHOLD = 0.5
Training Set: 
Document count Tuple2{t1=1152, t2=497}
Spam count Tuple2{t1=250, t2=246}
True positives 246
False negatives 4
False positives 497
True negatives 655
Precision 0.3310901749663526
Recall 0.984

Validation Set
Document count Tuple2{t1=1339, t2=1199}
Spam count Tuple2{t1=210, t2=208}
True positives 208
False negatives 2
False positives 1199
True negatives 140
Precision 0.1478322672352523
Recall 0.9904761904761905

Test Set
Document count Tuple2{t1=1511, t2=1431}
Spam count Tuple2{t1=223, t2=221}
True positives 221
False negatives 2
False positives 1431
True negatives 80
Precision 0.1337772397094431
Recall 0.9910313901345291

```
```
> Task :run
BayesSpamFilterTrain.PR_S = 0.8
BayesSpamFilter.THREASHOLD = 0.9
Training Set: 
Document count Tuple2{t1=1152, t2=429}
Spam count Tuple2{t1=250, t2=190}
True positives 190
False negatives 60
False positives 429
True negatives 723
Precision 0.3069466882067851
Recall 0.76

Validation Set
Document count Tuple2{t1=1339, t2=1085}
Spam count Tuple2{t1=210, t2=184}
True positives 184
False negatives 26
False positives 1085
True negatives 254
Precision 0.1449960598896769
Recall 0.8761904761904762

Test Set
Document count Tuple2{t1=1511, t2=1308}
Spam count Tuple2{t1=223, t2=194}
True positives 194
False negatives 29
False positives 1308
True negatives 203
Precision 0.12916111850865514
Recall 0.8699551569506726
```


