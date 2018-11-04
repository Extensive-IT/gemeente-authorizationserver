#!/bin/sh
# -H 'authorization: Basic c3ByaW5nLXNlY3VyaXR5LW9hdXRoMi1yZWFkLXdyaXRlLWNsaWVudDpzcHJpbmctc2VjdXJpdHktb2F1dGgyLXJlYWQtd3JpdGUtY2xpZW50LXBhc3N3b3JkMTIzNA==' \
curl -v -X POST \
  -H 'Authorization: Basic Zmlyc3QtYXBwOjEyMzQ1' \
	http://localhost:8080/oauth/authorize \
	-F response_type=code \
  -F client_id=first-app \
  -F redirect_uri=http://localhost:8081/redirect \
  -F scope=apiX \
  -F state=1234zyx
