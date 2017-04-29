export class Person {

    static fromPerson(person: Person): Person {
        return {
            dn: person.dn,
            fullName: person.fullName,
            firstName: person.firstName,
            lastName: person.lastName,
            email: person.email,
            uid: person.uid,
            password: person.password
        };
    };

    dn: string;
    fullName: string;
    firstName: string;
    lastName: string;
    email: string;
    uid: string;
    password: string;
}
