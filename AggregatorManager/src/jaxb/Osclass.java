//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.18 at 05:55:47 AM EET 
//


package jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


// TODO: Auto-generated Javadoc
/**
 * The Class Osclass.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "cpe"
})
@XmlRootElement(name = "osclass")
public class Osclass {

    @XmlAttribute(name = "vendor", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String vendor;
    @XmlAttribute(name = "osgen")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String osgen;
    @XmlAttribute(name = "type")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String type;
    @XmlAttribute(name = "accuracy", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String accuracy;
    @XmlAttribute(name = "osfamily", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String osfamily;
    protected List<Cpe> cpe;

    /**
     * Gets the value of the vendor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * Sets the value of the vendor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVendor(String value) {
        this.vendor = value;
    }

    /**
     * Gets the value of the osgen property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOsgen() {
        return osgen;
    }

    /**
     * Sets the value of the osgen property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOsgen(String value) {
        this.osgen = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the accuracy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccuracy() {
        return accuracy;
    }

    /**
     * Sets the value of the accuracy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccuracy(String value) {
        this.accuracy = value;
    }

    /**
     * Gets the value of the osfamily property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOsfamily() {
        return osfamily;
    }

    /**
     * Sets the value of the osfamily property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOsfamily(String value) {
        this.osfamily = value;
    }

    /**
     * Gets the value of the cpe property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cpe property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCpe().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Cpe }
     *
     * @return the cpe
     */
    public List<Cpe> getCpe() {
        if (cpe == null) {
            cpe = new ArrayList<Cpe>();
        }
        return this.cpe;
    }

}
