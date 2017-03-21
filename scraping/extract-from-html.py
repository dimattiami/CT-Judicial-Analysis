#!/usr/bin/python
import os
import sys
from bs4 import BeautifulSoup

#This script looks at our saved HTML dockets and extracts information about the case. Output will be loaded into SQL. NOTE: very messy. This process was performed on a Win10 machine.

if len(sys.argv) != 4:
  print "usage: "+sys.argv[0]+" inDir chargesOut offenderChargesOut"
  sys.exit(1)

inDir=sys.argv[1]
chargesOutFile=sys.argv[2]
offenderChargeOutFile=sys.argv[3]

print inDir+","+chargesOutFile+","+offenderChargeOutFile

chargesOutTarget = open(chargesOutFile, 'w')
offenderChargeOutTarget = open(offenderChargeOutFile, 'w')

status = 0
for filename in os.listdir(os.getcwd()+'/'+inDir):
  if filename.endswith('.html') and  not filename.endswith('T.html'):
    status += 1
    print "Working with record: "+str(status)
  #  name="",birthyear="",attourney=""
  #  docketno="",court="",costs="",costStatus="",arrestingagency="",arrestdate="",sentenceddate=""
    soup = BeautifulSoup(open(inDir+'/'+filename), 'lxml')

    try:
	  name=soup.find('span',id='_ctl0_cphBody_lblDefendant').contents[0].strip()
    except AttributeError:
	  continue
    try:
	  birthyear=soup.find('span',id='_ctl0_cphBody_lblDefendantBirthDate').contents[0].strip()
    except IndexError:
	  birthyear=""
    try:
	  attourney=soup.find('span',id='_ctl0_cphBody_lblDefendantAttorney').contents[0].strip()
    except IndexError:
	  attourney=""
    
    try:
	  docketno=soup.find('span',id='_ctl0_cphBody_lblDocketNo').contents[0].strip()
    except IndexError:
	  pass
    try:
	  court=soup.find('span',id='_ctl0_cphBody_lblCourt').contents[0].strip()
    except IndexError:
	  court=""
    try:
	  costs=' '.join(soup.find('span',id='_ctl0_cphBody_lblCost').contents).strip()
    except IndexError:
	  costs=""
    try:
	  costsStatus=' '.join(soup.find('span',id='_ctl0_cphBody_Label4').contents).strip()
    except IndexError:
	  costsStatus=""
    try:
	  arrestingagency=soup.find('span',id='_ctl0_cphBody_lblArrestingAgency').contents[0].strip()
    except IndexError:
	  arrestingagency=""
    try:
	  arrestdate=soup.find('span',id='_ctl0_cphBody_lblArrestDate').contents[0].strip()
    except IndexError:
	  arrestdate=""
    try:
	  sentenceddate=soup.find('span',id='_ctl0_cphBody_lblSentDate').contents[0].strip()
    except IndexError:
	  sentenceddate=""

    charges=soup.find('table',id='_ctl0_cphBody_Datagrid1')
    overallInfo=""
    try:
      overallInfo=soup.find('table',id='_ctl0_cphBody_Datagrid2').findAll('td')
    except AttributeError:
	  pass
	  #    overallInfo=soup.find('table',id='_ctl0_cphBody_Datagrid2')
#    print "~~~~~~~~~~~~~~~~" 
#    print name+','+birthyear+','+attourney+','+docketno+','+court+','+costs+','+coststatus+','+arrestingagency+','+arrestdate+','+sentenceddate
    #print charges.findChildren()[1]
    splitChar='|'

    record=""
    i=-1
    extract=charges.findAll('tr',{'class':['grdRow','grdRowAlt']})
    for item in extract:
      i+=1
      if i%2 == 0:
#       record+=name+splitChar+birthyear+splitChar+docketno
        personid=name.split(" ")
	record+=docketno+splitChar#+personid[0]+personid[1]+birthyear+splitChar
        record+=str(item.td.contents[0]).strip()+splitChar
        record+=str(item.span.contents[0]).strip()+splitChar
        for c in range(3,12):
          record+=item.contents[c].contents[0].strip()+splitChar
      else:
		try:
		  record+=str(item.td.contents[1].strip())
		except IndexError:
		  pass
		if i+1 != len(extract):
    #     record+=str(i)+"..."+str(len(extract))
		  record+='\n'
#    print record # Charges
    chargesOutTarget.write(record+'\n')
    overall=""
    q = 0
    tempSplitChar=','
    for o in overallInfo:
      q += 1
      if q!=1:
        if q==2:
          overall+=o.contents[0].strip()
        else:
          overall+=tempSplitChar+o.contents[0].strip()

    offCharge=personid[0]+personid[1]+birthyear+splitChar+docketno+splitChar+attourney+splitChar+court+splitChar+costs+splitChar+costsStatus+splitChar+arrestingagency+splitChar+arrestdate+splitChar+sentenceddate+splitChar+overall # Offender-Charges
    offenderChargeOutTarget.write(offCharge+'\n')
chargesOutTarget.close()
offenderChargeOutTarget.close()

#    sysexit(1)
