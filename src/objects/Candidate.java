package objects;

public class Candidate implements Comparable<Candidate>
{
    private byte key;
    private byte[] result;
    private Float score;

    public byte getKey()
    {
        return key;
    }

    public void setKey(byte key)
    {
        this.key = key;
    }

    public byte[] getResult()
    {
        return result;
    }

    public void setResult(byte[] result)
    {
        this.result = result;
    }

    public Float getScore()
    {
        return score;
    }

    public void setScore(Float score)
    {
        this.score = score;
    }

    public String getResultAsString()
    {
        return new String(result).trim();
    }

    @Override
    public int compareTo(Candidate o)
    {
        return score.compareTo(o.score);
    }

    @Override
    public String toString() {
        return "Key - " + key + ", Score - " + score;
    }
}
