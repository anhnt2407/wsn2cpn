package br.cin.ufpe.wsn2cpn.layer;

/**
 *
 * @author avld
 */
public class LayerProperty
{
    private String name;
    private String nick;
    private String type;
    private String defaultValue;
    private boolean hidden;
    private String description;

    public LayerProperty()
    {
        // do nothing
    }
    
    public LayerProperty( String name , String type )
    {
        this.name = name;
        this.type = type;
    }
    
    public LayerProperty( String name , String type , String value )
    {
        this.name = name;
        this.type = type;
        this.defaultValue = value;
    }

    public String getDefaultValue()
    {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    public boolean isHidden()
    {
        return hidden;
    }

    public void setHidden(boolean hidden)
    {
        this.hidden = hidden;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getNick()
    {
        return nick;
    }

    public void setNick(String nick)
    {
        this.nick = nick;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
    
}
