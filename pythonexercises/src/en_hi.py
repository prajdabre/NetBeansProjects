#! /usr/bin/python

# To change this template, choose Tools | Templates
# and open the template in the editor.

import sys
import requests
import json
proxies = {
  "http": "http://11305R001:01041989@netmon.iitb.ac.in:80"
}
headers = {'Referer':'http://sanskrit.uohyd.ernet.in/scl/morph/index.html','User-Agent':'Mozilla/5.0 (Windows NT 6.1; WOW64; rv:22.0) Gecko/20100101 Firefox/22.0'}
lang="hi"
r=requests.get("http://sanskrit.uohyd.ernet.in/cgi-bin/scl/morph/morph.cgi?encoding=Unicode&morfword=%E0%A4%B5%E0%A4%BF%E0%A4%AD%E0%A4%95%E0%A5%8D%E0%A4%A4%E0%A4%BF%E0%A4%83", proxies=proxies,headers=headers)

print r.text

