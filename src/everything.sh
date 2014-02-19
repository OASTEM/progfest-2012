#!/bin/bash

echo "please pick yo program"
select program_name in "ppp" "imgseg" "naunav";
do
	case $program_name in
		ppp ) echo "enter [number] [base]";
			java -jar 
