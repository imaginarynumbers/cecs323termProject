import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StateAccessProject extends State {

	Project project;

	StateAccessProject(Project project) {
		this.project = project;
	}

	void deleteProject() throws SQLException {
		this.project.delete(this.db);
		System.out.println("Deleted project " + this.project.title);
	}

	void insertUserStory() throws SQLException {
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());

		String as = this.scan.raw_input("As: ");
		String wantTo = this.scan.raw_input("I want to : ");
		String because = this.scan.raw_input("Because: ");
		int priority = Integer.parseInt(this.scan.raw_input("Priority (int): "));
		String status = this.scan.raw_input("Status: "); // Should be an enum here
		UserStory us = new UserStory(0, as, wantTo, because, priority, status, date, this.project.projectId);
		us.insert(this.db);
	}

	@Override
	State update() throws SQLException {
		String[] options = { "Delete project", "Add Sprint associated with project", "Add UserStory for project",
				"View Sprints", "View UserStories", "Return to main" };
		int rep = this.scan.showOptions("Project " + this.project.title, options);
		switch (rep) {
		case 1:
			String y = this.scan.raw_input("Are you sure you want to DELETE this project? (y/n) ");
			if (y.toLowerCase().equals("y")) {
				this.deleteProject();
				return new StateMain();
			}
			break;

		case 2:
			return new StateAddSprint(this.project);
			break;

		case 3:
			this.insertUserStory();
			break;

		case 4:
			this.db.printSprints(this.project.projectId);
			break;

		case 5:
			this.db.printUserStories(this.project.projectId);
			break;

		default:
			return new StateMain();
		}
		return null;
	}
}
