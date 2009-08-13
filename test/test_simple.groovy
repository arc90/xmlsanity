import com.arc90.sanevalidator.Validator

println new Validator(new File(args[0])).validate(new File(args[1]))