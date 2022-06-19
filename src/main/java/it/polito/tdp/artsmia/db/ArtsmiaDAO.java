package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.CoppiaIdPeso;
import it.polito.tdp.artsmia.model.Exhibition;

public class ArtsmiaDAO {
	
	private Map<Integer, Artist> idMapArtists = new HashMap<Integer, Artist>();

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Exhibition> listExhibitions() {
		
		String sql = "SELECT * from exhibitions";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Exhibition exObj = new Exhibition(res.getInt("exhibition_id"), res.getString("exhibition_department"), res.getString("exhibition_title"), 
						res.getInt("begin"), res.getInt("end"));
				
				result.add(exObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getRuoli()
	{
		String sql = "SELECT DISTINCT role "
				+ "FROM authorship "
				+ "ORDER BY role";
		
		List<String> result = new ArrayList<String>();
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(res.getString("role"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Artist> getVertici(String r)
	{
		String sql = "SELECT distinct a.* "
				+ "FROM authorship au, artists a "
				+ "WHERE au.artist_id = a.artist_id AND au.role = ?";
		
		List<Artist> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, r);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Artist a = new Artist(res.getInt("artist_id"), res.getString("name"));
				
				this.idMapArtists.put(a.getArtist_id(), a);
				result.add(a);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<CoppiaIdPeso> getArchi(String r)
	{
		String sql = "SELECT a0.artist_id, a1.artist_id, COUNT(*) as peso "
				+ "FROM authorship a0, authorship a1, exhibition_objects e0, exhibition_objects e1 "
				+ "WHERE a0.object_id = e0.object_id AND a1.object_id = e1.object_id AND e0.exhibition_id = e1.exhibition_id "
				+ "		AND a0.role = a1.role AND a0.role = ? "
				+ "		AND a0.artist_id > a1.artist_id "
				+ "GROUP BY a0.artist_id, a1.artist_id;";
		
		List<CoppiaIdPeso> result = new ArrayList<CoppiaIdPeso>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, r);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				CoppiaIdPeso arco = new CoppiaIdPeso(idMapArtists.get(res.getInt("a0.artist_id")), idMapArtists.get(res.getInt("a1.artist_id")), res.getInt("peso"));
				
				result.add(arco);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
