import sys
sys.path.append('../dist/com.arc90.xml.validation.jar')
from com.arc90.sanevalidator import Validator
from java.io import File

print Validator(File(sys.argv[1])).validate(File(sys.argv[2]))