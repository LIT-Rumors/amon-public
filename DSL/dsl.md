
AMon Domain-Specific Monitoring Adaptation Language

To facilitate the specification of adaptation rules for a runtime monitoring environment, we created a domain-specific language that provides capabilities for defining different types of adaptation rules and specifying assumptions about the monitoring data.

We include two types of rules:

-. Preplanned Rules that are triggered based on different states of the system, or of its devices (e.g., a UAV being in a "takeoff" state versus "flying")
-. Ubiquitous Rules that are triggered by exceptions (e.g., a low battery warning) or other factors.

In addition to the specification of rules, we also support the description of 
- Assumptions about the system and its environment.
- Default Values



## Preplanned Rules

Preplanned adaptations of the monitoring environment may occur as the CPS  changes state, and these adaptations may require changing the monitoring location from the edge device (e.g., onboard the UAV) to a  centralized monitoring component.
Such rules can either apply _globally_, or to _specific devices_ (e.g., a group of UAVs performing a joint mission, or even individual devices identified by their respective id).
The _Context_ element in the _Trigger_ condition indicates the UAV's current state. 
Subsequently, each  _Monitor_ entry denotes a property item (i.e., a message) that is collected and distributed by the monitoring system. 


![image](https://user-images.githubusercontent.com/24531486/173364191-733f8ffb-e904-4812-9ef8-4f8a11567f8e.png)



Monitor specifications define the _Scope_ i.e., whether the data is processed locally on the device _scope local_, or sent to a central server (_scope central_) for processing, constraint checking, or further analysis.
The _Period_ further specifies how often data is sent from, for example, the edge device to the central server.


## Ubiquitous Rules

![image](https://user-images.githubusercontent.com/24531486/173364278-535dcc2c-35fc-43f8-894c-a72eba576a17.png)

![image](https://user-images.githubusercontent.com/24531486/173364379-012afc36-6e4b-4df1-b5a3-3a820233cd7f.png)

![image](https://user-images.githubusercontent.com/24531486/173364458-a34c5cc8-3c61-4d3c-aad0-ac93b82f4d19.png)



## Default Values

![image](https://user-images.githubusercontent.com/24531486/173364525-2e713038-12d3-49a1-8aa2-ee9617648f91.png)



## Assumptions

![image](https://user-images.githubusercontent.com/24531486/173364593-be9ed830-9398-4a40-ba70-3d781ac7620f.png)



### Grammar

[XText DSL File](Dsl.xtext)


```Model: 	namespace=Namespace contracts+=Assumption* defaults+=DefaultValues* prules+=MonitoringRule*  grules+=GlobalRule* constraints+=ConstraintRule*;```

``Namespace:	'namespace' namespace=STRING ';';``


``Assumption: 	'Assumption' name=STRING frequency=MinFrequency  ';';``

``DefaultValues:	'Default' name=STRING value=State ';';``

`` State: 	state=(OFF|KEEP);``



``MinFrequency:	'MIN_PERIOD:' frequency=DOUBLE 'seconds';``

``ConstraintRule:	'Constraint' name=STRING scope=Scope  'requiresmonitor' monitors+=STRING* ';';``

``Applicability:	'applies' trigger=(ApplicationID | ApplicationGlobal | ApplicationGroup);``

``ApplicationID:	'for' 'device' '[' event=STRING ']';``

``ApplicationGroup:	'for' 'group' '(' group=STRING ')';``

``ApplicationGlobal:	sc='forall';``

``Scope:	'scope' scope=('local' | 'central');``

``MonitoringRule:	'Rule' 'PREPLANNED' name=STRING applicability=Applicability  trigger=Trigger monitors+=Monitor* ';';``
	
``GlobalRule:	'Rule' 'UBIQUITOUS' name=STRING applicability=Applicability ('salience' salience=INT)?  trigger=Trigger monitors+=Monitor*  ';';``
	

``Trigger:	'Trigger' trigger=(Event | Context | Data);``

``Context:	'Context' '[' context=STRING ']' status?=Status?;``

``Status:	'ENTRY' | 'EXIT';``

``Event:	'Event' '[' event=STRING ']' ('and' 'Event' '[' ontop+=STRING ']')* ;``

``Data:	'Data' '[' value=STRING 'when' condition=ValueCondition ']';``


``Monitor:	'Monitor' name=STRING ':' changes+=ChangeType*;``

``ChangeType:	ChangeFrequency | ChangeState | ChangeScope | ChangeReportingFrequency;``

``ChangeScope:	'CHANGE_SCOPE:' scope=Scope;``

``ChangeState:	'CHANGE_STATE:' scope=(ON | OFF);``

``ChangeFrequency:	'CHANGE_PERIOD:' 'every' frequency=DOUBLE 'seconds';``

``ChangeReportingFrequency:	'CHANGE_REPORTING_PERIOD:' 'every' frequency=DOUBLE 'seconds';``
