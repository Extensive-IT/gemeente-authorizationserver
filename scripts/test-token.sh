#!/bin/sh
curl -v -X POST \
	http://localhost:8080/oauth/token \
	-H 'Authorization: Basic Zmlyc3QtYXBwOjEyMzQ1' \
	-F grant_type=password \
	-F username=john \
	-F password=123 \
	-F client_id=first-app
