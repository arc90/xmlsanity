import com.arc90.sanevalidator.Validator

validator = new Validator(new File(args[0]))

result = validator.validate(new File(args[1]))

println result