package br.cin.ufpe.wsn2cpn.layer;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author avld
 */
public class LayerConfiguration
{
    private String name;                // layer name
    private String layer;               // layer
    private String developer;           // developer
    private String description;         // description
    
    private List<LayerProperty> variableList;
    private List<LayerProperty> nodePropertiesList;

    public LayerConfiguration()
    {
        variableList = new ArrayList<LayerProperty>();
        nodePropertiesList = new ArrayList<LayerProperty>();
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDeveloper()
    {
        return developer;
    }

    public void setDeveloper(String developer)
    {
        this.developer = developer;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<LayerProperty> getNodeProperties()
    {
        return nodePropertiesList;
    }

    public void setNodeProperties(List<LayerProperty> properties)
    {
        this.nodePropertiesList = properties;
    }

    public String getLayer()
    {
        return layer;
    }

    public void setLayer(String layer)
    {
        this.layer = layer;
    }

    public List<LayerProperty> getVariableList()
    {
        return variableList;
    }

    public void setVariableList(List<LayerProperty> variableList)
    {
        this.variableList = variableList;
    }
    
    @Override
    public String toString()
    {
        return getName();
    }
    
}
