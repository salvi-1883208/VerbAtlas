package it.uniroma1.nlp.kb;

public abstract class ResourceID
{
	private String id;

	
	// Classe degli id boh
	public ResourceID(String id)
	{
		this.id = id;
	}

	public String getId()
	{
		return id;
	}

	@Override
	public String toString()
	{
		return id;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (this.getClass() != o.getClass())
			return false;

		ResourceID id = (ResourceID) o;

		return this.id.equals(id.getId());
	}
}
