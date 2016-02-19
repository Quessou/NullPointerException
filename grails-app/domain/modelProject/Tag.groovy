package modelProject

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString


@EqualsAndHashCode(includes='name')
class Tag {

	String name;

    static constraints = {
    }
}
