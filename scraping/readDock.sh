#!/bin/bash

#This script passes docket number from input files to docket-post.js

counta=0;
input="partaa"
#input="15"
while IFS= read -r l
do
#  echo "$var"
  let "counta++"
  echo "EXECUTING $counta: $l";
  /home/ec2-user/phantomjs/phantomjs-2.1.1-linux-x86_64/bin/phantomjs docket-post.js "$l";

done < "$input"
