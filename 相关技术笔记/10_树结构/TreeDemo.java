
/*
* 本例使用 Hutool的Tree相关工具组装树结构
*/
public class TreeDemo {

	public List<Tree<String>> selectProductTree(String area) {
		/*
		* 1，查询出数据库内的数据集合，数据中code,parentCode都已按照关系设置完毕
		*/
		List<ProductCategory> productCategoryList = lambdaQuery().eq(ObjectUtil.isNotEmpty(area),ProductCategory::getArea, area).list();
		/*
		* 2，新建一个TreeNode<String>集合，因为ProductCategory中code，parentCode是String类型，所以用String泛型
		* 也可以用其他的泛型
		*/
		List<TreeNode<String>> treeNodeList = new ArrayList<>();
		/*
		* 3，把productCategoryList集合中的对应树形赋值给TreeNode
		*/
		for(ProductCategory p : productCategoryList){
			//3.1 设置固定属性值
			TreeNode<String> treeNode = new TreeNode<>(p.getCode(), p.getParentCode(), p.getName(), p.getSortOrder());
			//3.2 添加自定义的属性值
			Map<String,Object> extra = new HashMap<>();
			extra.put("type",p.getType());
			treeNode.setExtra(extra);
			treeNodeList.add(treeNode);

		}

		return TreeUtil.build(treeNodeList,"00");
	}


}