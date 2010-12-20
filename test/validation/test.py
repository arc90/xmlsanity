import sys
sys.path.append('../dist/com.arc90.xml.validation.jar')
from com.arc90.sanevalidator import Validator
from java.io import File

schemaFile = File(sys.argv[1])
contentFile = File(sys.argv[2])

validator = Validator(schemaFile)

print validator.validate(contentFile)