// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
//Parse.Cloud.define("hello", function(request, response) {
//  response.success("Hello world!");
//});

Parse.Cloud.afterSave("Delivery", function(request) {
	
	
	//Test Code to check
	var objId = request.object.id;
	
	if(!request.object.existed()) {
	var Delivery = Parse.Object.extend("Delivery");
	var query = new Parse.Query(Delivery);
	query.get(objId, {
		success: function(ofd) {
			// The object was retrieved successfully.
		
			var deliveryTo=ofd.get("deliveryTo");
			var schedTime=ofd.get("scheduledTime");
			var delAddress=ofd.get("deliveryAddress");
			var status = ofd.get("status");
			var pkgInfo = ofd.get("pkgInfo");
			var deliveryBy = ofd.get("deliveryBy");

		
			//Code for sending the push notification to the specific device
			var query = new Parse.Query(Parse.Installation);
			query.equalTo('userId', deliveryTo);			
			
			Parse.Push.send({
				where: query, // Set our Installation query
				data: {
					//action: "com.parse.push.intent.OPEN",
					alert: "Hey, your package is on the way...",
					delTo: "" + deliveryTo,
					delBy: "" + deliveryBy,
					delAddress: "" + delAddress,
					schTime: "" + schedTime,
					st: "" + status,
					pkgInf: "" + pkgInfo,
					objId: "" + objId
				}
			}, {
				success: function() {
					// Push was successful
					alert('Push successful!...');
					
				},
				error: function(error) {
					// Handle error
					alert('Error sending the push!...');
					
				}
			});
		},
		error: function(object, error) {
			// The object was not retrieved successfully.
			// error is a Parse.Error with an error code and message.
			alert('Error retrieving the object');
		}
	
	});
	}
	
});	