package people

import java.time.Period

data class Person(var familyName :String, val givenNames :Array<String>,
                  var gender :Gender, val birthday :PartialDate,
                  val parents :MutableList<Person> =mutableListOf(),
                  var title :String? =null,
                  var spouse :Person? =null,
                  val children :MutableList<Person> =mutableListOf(),
                  var deathDay :PartialDate? =null) {

    override fun toString() = toString(Relation.Self)

    fun toString(relation :Relation) = "${relation.title(gender)} ${title ?:""} ${givenNames.joinToString(" ")} $familyName (*$birthday" +
            if(deathDay!=null) ", ✝$deathDay)"
            else ")"

    fun getAge(today :PartialDate =PartialDate.today()) :Period {
        if (today.year>birthday.year+1u)
            return Period.of(today.year.toInt() -birthday.year.toInt() -
                    if (today.month!=null&&birthday.month!=null&&today.month<birthday.month) 1 else 0,
                0,0)
        if (today.year>birthday.year && today.month!=null && birthday.month!=null && today.month>=birthday.month)
            return Period.of(today.year.toInt() -birthday.year.toInt(), 0,0)
        if (today.month!=null && birthday.month!=null)
            if (today.month>birthday.month+1u || today.month>birthday.month&&today.day!=null&&birthday.day!=null&&today.day>=birthday.day)
                return Period.of(0, today.month.toInt() -birthday.month.toInt(), 0)
            else if (today.day!=null && birthday.day!=null)
                return Period.of(0,0,today.day.toInt() -birthday.day.toInt())
        return Period.ZERO
    }

    val ageInYears :Int
        get() = getAge().years

    val married :Boolean
        get() = spouse!=null

    fun describe() = "$this, "+parents.joinToString(",\n") { it.toString(Relation.Parent) } +
            (if (spouse!=null) ", ${spouse?.toString(Relation.Spouse)}"  else "") +
            children.joinToString { it.toString(Relation.Child) }

    override fun equals(other: Any?): Boolean {
        if (other !is Person)
            return false
        return gender==other.gender && birthday==other.birthday && givenNames contentEquals other.givenNames
    }

    override fun hashCode() = 3+ gender.hashCode() +31*birthday.hashCode() +997*givenNames.contentHashCode()
}

enum class Gender(val title :String) {
    UNKNOWN("XX."), MALE("Mr."), FEMALE("Ms."), OTHER("")
}

fun Person.marry(spouse :Person, type :MarriageType =MarriageType.European) {
    check(ageInYears >= 17) { "You are too young for marriage" }
    require(spouse.ageInYears >= 17) { "Your partner is too young for marriage" }
    check(!married) { "You are not single" }
    require(!spouse.married) { "Your partner is not single" }
    this.spouse = spouse;  spouse.spouse = this
    spouse.familyName = type.deriveFamilyName(spouse.familyName, familyName)
    println("$this married $spouse")
}

fun Person.giveBirth(givenNames :Array<String>, gender :Gender, birthday :PartialDate) :Person {
    check(getAge(birthday).years >= 15) { "You are too young to have a child" }
    if (spouse!=null)
        require(spouse!!.getAge(birthday).years >= 15) { "Your partner is too young to have a child" }
    val child = Person(familyName, givenNames, gender, birthday)
    child.parents.add(this)
    children.add(child)
    if (spouse!=null) {
        child.parents.add(spouse!!)
        spouse!!.children.add(child)
    }
    return child
}

fun Person.findSiblings() = parents.flatMap { it.children }.filter { it!=this }.toSet()

enum class MarriageType {
    European {
        override fun deriveFamilyName(familyName: String, partnersFamilyName: String) = partnersFamilyName
    }, Modern {
        override fun deriveFamilyName(familyName: String, partnersFamilyName: String) = "$familyName-$partnersFamilyName"
    }, Asian {
        override fun deriveFamilyName(familyName: String, partnersFamilyName: String) = familyName
    };

    abstract fun deriveFamilyName(familyName :String, partnersFamilyName :String) :String
}

enum class Relation {
    Self {
        override fun title(gender: Gender) = gender.title
    }, Parent {
        override fun title(gender: Gender) = when (gender) {
            Gender.FEMALE -> "mother"
            else -> "father"
        }
    }, Spouse {
        override fun title(gender: Gender) = when (gender) {
            Gender.FEMALE -> "wife"
            Gender.MALE -> "husband"
            else -> "partner"
        }
    }, Child {
        override fun title(gender: Gender) = when (gender) {
            Gender.FEMALE -> "daughter"
            Gender.MALE -> "son"
            else -> "child"
        }
    };

    abstract fun title(gender :Gender) :String
}
