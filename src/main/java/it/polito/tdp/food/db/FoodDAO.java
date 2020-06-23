package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Coppia;

public class FoodDAO {

	public List<Food> listAllFood(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_id"),
							res.getInt("food_code"),
							res.getString("display_name"), 
							res.getInt("portion_default"), 
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"),
							res.getDouble("calories")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

	}
	
	public void listAllCondiment(Map<Integer, Condiment> idMapCondiments){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				idMapCondiments.put(res.getInt("condiment_id"), new Condiment(res.getInt("condiment_id"),
						res.getInt("food_code"),
						res.getString("display_name"), 
						res.getString("condiment_portion_size"), 
						res.getDouble("condiment_calories")));
			}
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public List<Condiment> getCondimentMaxCalories (double calories, Map<Integer, Condiment> idMapCondiments) {
		String sql = "SELECT condiment_id FROM condiment WHERE condiment_calories < ?";
		List<Condiment> list = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, calories);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				Condiment condiment = idMapCondiments.get(res.getInt("condiment_id"));
				list.add(condiment);
			}
			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Coppia> getCoppieCondiments(double calories, Map<Integer, Condiment> idMapCondiments){
		String sql = "SELECT c1.condiment_id, c2.condiment_id, COUNT(*) AS C " +
						"FROM food_condiment AS t1, food_condiment AS t2, condiment AS c1, condiment AS c2 " +
						"WHERE c1.food_code=t1.condiment_food_code AND c2.food_code=t2.condiment_food_code " +
						"AND t1.condiment_food_code > t2.condiment_food_code AND t1.food_code=t2.food_code " +
						"AND c1.condiment_calories < ? AND c2.condiment_calories < ? " +
						"GROUP BY c1.condiment_id, c2.condiment_id";
		List<Coppia> list = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, calories);
			st.setDouble(2, calories);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				Condiment c1 = idMapCondiments.get(res.getInt("c1.condiment_id"));
				Condiment c2 = idMapCondiments.get(res.getInt("c2.condiment_id"));
				Coppia c = new Coppia(c1, c2, res.getInt("C"));
				list.add(c);
			}
			conn.close();
			return list;
		}catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public Integer getNumCibiCondiment(Condiment condiment) {
		String sql = "SELECT COUNT(DISTINCT fc.food_code) AS c " +
					"FROM food_condiment AS fc, condiment AS co " +
					"WHERE fc.condiment_food_code = co.food_code AND co.condiment_id = ? " +
					"GROUP BY co.condiment_id, fc.condiment_food_code";
		Integer i = null;
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, condiment.getCondiment_id());
			ResultSet res = st.executeQuery();
			while(res.next())
				i = res.getInt("c");
			conn.close();
			return i;
		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
}

