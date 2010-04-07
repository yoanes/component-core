//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.3-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.03.29 at 12:40:27 PM EST 
//


package au.com.sensis.mobile.web.component.core.device.generated;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *                 Container for one or more device configurations.
 *             
 * 
 * <p>Java class for DeviceConfigs complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DeviceConfigs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="identifiedDeviceConfig" type="{http://mobile.sensis.com.au/web/component/core/device}IdentifiedDeviceConfig" maxOccurs="unbounded"/>
 *         &lt;element name="defaultDeviceConfig" type="{http://mobile.sensis.com.au/web/component/core/device}AbstractDeviceConfig"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DeviceConfigs", propOrder = {
    "identifiedDeviceConfig",
    "defaultDeviceConfig"
})
@Generated(value = "com.sun.tools.xjc.Driver", date = "2010-03-29T12:40:27+10:00", comments = "JAXB RI v2.1.3-b01-fcs")
public class DeviceConfigs {

    @XmlElement(required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2010-03-29T12:40:27+10:00", comments = "JAXB RI v2.1.3-b01-fcs")
    protected List<IdentifiedDeviceConfig> identifiedDeviceConfig;
    @XmlElement(required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2010-03-29T12:40:27+10:00", comments = "JAXB RI v2.1.3-b01-fcs")
    protected AbstractDeviceConfig defaultDeviceConfig;

    /**
     * Gets the value of the identifiedDeviceConfig property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the identifiedDeviceConfig property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdentifiedDeviceConfig().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IdentifiedDeviceConfig }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2010-03-29T12:40:27+10:00", comments = "JAXB RI v2.1.3-b01-fcs")
    public List<IdentifiedDeviceConfig> getIdentifiedDeviceConfig() {
        if (identifiedDeviceConfig == null) {
            identifiedDeviceConfig = new ArrayList<IdentifiedDeviceConfig>();
        }
        return this.identifiedDeviceConfig;
    }

    /**
     * Gets the value of the defaultDeviceConfig property.
     * 
     * @return
     *     possible object is
     *     {@link AbstractDeviceConfig }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2010-03-29T12:40:27+10:00", comments = "JAXB RI v2.1.3-b01-fcs")
    public AbstractDeviceConfig getDefaultDeviceConfig() {
        return defaultDeviceConfig;
    }

    /**
     * Sets the value of the defaultDeviceConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link AbstractDeviceConfig }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2010-03-29T12:40:27+10:00", comments = "JAXB RI v2.1.3-b01-fcs")
    public void setDefaultDeviceConfig(AbstractDeviceConfig value) {
        this.defaultDeviceConfig = value;
    }

}