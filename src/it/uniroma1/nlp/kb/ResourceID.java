package it.uniroma1.nlp.kb;

/**
 * Classe astratta che rappresenta i vari tipi di ID contenuti all'intero della
 * risorsa VerbAtlas.
 * 
 * @author Salvi Marco
 */
public abstract class ResourceID
{
	private String id;

	/**
	 * Costruttore della classe
	 * 
	 * @param String id
	 */
	public ResourceID(String id)
	{
		this.id = id;
	}

	/**
	 * Restituisce l'id come Stringa.
	 * 
	 * @return String id
	 */
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

	@Override
	public int hashCode()
	{
		return id.hashCode();
	}
}
