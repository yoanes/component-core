//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.3-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.06.01 at 10:59:28 AM EST 
//


package au.com.sensis.mobile.web.component.core.device.generated;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *                 Configuration for devices that have a user agent matching a given regex. 
 *             
 * 
 * <p>Java class for UserAgentDeviceConfig complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UserAgentDeviceConfig">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="useragentRegex" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="deviceConfig" type="{http://mobile.sensis.com.au/web/component/core/device}AbstractDeviceConfig"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserAgentDeviceConfig", propOrder = {
    "useragentRegex",
    "deviceConfig"
})
@Generated(value = "com.sun.tools.xjc.Driver", date = "2010-06-01T10:59:28+10:00", comments = "JAXB RI v2.1.3-b01-fcs")
public class UserAgentDeviceConfig {

    @XmlElement(required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2010-06-01T10:59:28+10:00", comments = "JAXB RI v2.1.3-b01-fcs")
    protected String useragentRegex;
    @XmlElement(required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2010-06-01T10:59:28+10:00", comments = "JAXB RI v2.1.3-b01-fcs")
    protected AbstractDeviceConfig deviceConfig;

    /**
     * Gets the value of the useragentRegex property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2010-06-01T10:59:28+10:00", comments = "JAXB RI v2.1.3-b01-fcs")
    public String getUseragentRegex() {
        return useragentRegex;
    }

    /**
     * Sets the value of the useragentRegex property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2010-06-01T10:59:28+10:00", comments = "JAXB RI v2.1.3-b01-fcs")
    public void setUseragentRegex(String value) {
        this.useragentRegex = value;
    }

    /**
     * Gets the value of the deviceConfig property.
     * 
     * @return
     *     possible object is
     *     {@link AbstractDeviceConfig }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2010-06-01T10:59:28+10:00", comments = "JAXB RI v2.1.3-b01-fcs")
    public AbstractDeviceConfig getDeviceConfig() {
        return deviceConfig;
    }

    /**
     * Sets the value of the deviceConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link AbstractDeviceConfig }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2010-06-01T10:59:28+10:00", comments = "JAXB RI v2.1.3-b01-fcs")
    public void setDeviceConfig(AbstractDeviceConfig value) {
        this.deviceConfig = value;
    }

}
