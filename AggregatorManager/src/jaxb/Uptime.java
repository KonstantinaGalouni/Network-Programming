//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.18 at 05:55:47 AM EET 
//


package jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


// TODO: Auto-generated Javadoc
/**
 * The Class Uptime.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "uptime")
public class Uptime {

    @XmlAttribute(name = "seconds", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String seconds;
    @XmlAttribute(name = "lastboot")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String lastboot;

    /**
     * Gets the value of the seconds property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSeconds() {
        return seconds;
    }

    /**
     * Sets the value of the seconds property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSeconds(String value) {
        this.seconds = value;
    }

    /**
     * Gets the value of the lastboot property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastboot() {
        return lastboot;
    }

    /**
     * Sets the value of the lastboot property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastboot(String value) {
        this.lastboot = value;
    }

}
