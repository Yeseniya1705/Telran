package telran.logs.bugs.jpa.entities;
import javax.persistence.*;
@Entity
@Table(name="artifacts")
public class Artifact {
	@Id
	@Column(name="artifactId")
String atrifactId;
	@ManyToOne
	@JoinColumn(name="programmerId" , nullable=false)
	Programmer programmer;
public Artifact() {
	
}
public Artifact(String atrifactId, Programmer programmer) {
	super();
	this.atrifactId = atrifactId;
	this.programmer = programmer;
}
public String getAtrifactId() {
	return atrifactId;
}
public Programmer getProgrammer() {
	return programmer;
}

}
